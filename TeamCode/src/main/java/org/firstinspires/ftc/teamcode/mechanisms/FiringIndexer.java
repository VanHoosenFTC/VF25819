package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Volts;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Configurable
public class FiringIndexer implements Mechanism {

    private static boolean debugLogsEnabled = false;

    // Power applied to the CR servo during a move
    public static double INDEX_POWER = 0.85;

    // Minimum power threshold when far from target
    public static double MIN_POWER = 0.18;

    // Target step per shot restored to 120 degrees (1 slot per index move)
    public static double STEP_DEGREES = 120.0;

    // Target tolerance window to complete move cleanly (degrees)
    public static double ANGLE_TOLERANCE_DEGREES = 3.0;

    // Settling band to start checking for motion completion (degrees)
    public static double SETTLING_BAND_DEGREES = 6.0;

    // Max time allowed to settle inside SETTLING_BAND_DEGREES before cutting power (seconds)
    public static double SETTLING_TIMEOUT_SECONDS = 0.15;

    // Flips delta direction if raw encoder degrees decrease when servo power is positive
    public static boolean INVERT_ENCODER = true;

    // Safety timeout per 120-degree index shot (seconds)
    public static double MOVE_TIMEOUT_SECONDS = 0.8;

    private final NextCRServo servo = new NextCRServo(RobotController.controlHub(), Config.firingIndexerServo);
    private final NextAnalogInput encoder = new NextAnalogInput(RobotController.controlHub(), Config.firingIndexerEncoder);

    private double targetAngleDegrees = 0.0;
    private double accumulatedDegrees = 0.0;
    private double lastRawAngle = 0.0;

    private long moveStartTimeNanos = 0;
    private long settlingStartTimeNanos = 0;
    private boolean isMoving = false;
    private boolean isSettling = false;
    private boolean isInitialized = false;

    public FiringIndexer() {
        // Safe constructor; NextAnalogInput handles lazy resolution under the hood
    }

    public double getRawDegrees() {
        return encoder.getValue() * 360.0;
    }

    public double getRawVoltage() {
        return encoder.getRawVoltage().into(Volts);
    }

    private void updateAccumulatedAngle() {
        double currentRaw = getRawDegrees();

        if (!isInitialized) {
            lastRawAngle = currentRaw;
            accumulatedDegrees = 0.0;
            targetAngleDegrees = 0.0;
            isInitialized = true;
            return;
        }

        double delta = currentRaw - lastRawAngle;

        if (delta < -180.0) {
            delta += 360.0;
        } else if (delta > 180.0) {
            delta -= 360.0;
        }

        if (INVERT_ENCODER) {
            delta = -delta;
        }

        accumulatedDegrees += delta;
        lastRawAngle = currentRaw;
    }

    public void resetPosition() {
        lastRawAngle = getRawDegrees();
        accumulatedDegrees = 0.0;
        targetAngleDegrees = 0.0;
        isMoving = false;
        isSettling = false;
        isInitialized = true;
        servo.setPower(0.0);
    }

    /**
     * Commands indexer forward by 120 degrees continuously.
     */
    public Command forward() {
        return instant(() -> {
            targetAngleDegrees += STEP_DEGREES;
            moveStartTimeNanos = System.nanoTime();
            isMoving = true;
            isSettling = false;
        });
    }

    /**
     * Commands indexer reverse by 120 degrees continuously.
     */
    public Command reverse() {
        return instant(() -> {
            targetAngleDegrees -= STEP_DEGREES;
            moveStartTimeNanos = System.nanoTime();
            isMoving = true;
            isSettling = false;
        });
    }

    /**
     * Stops indexer motion and snaps target to the nearest 120-degree grid index.
     */
    public Command stop() {
        return instant(() -> {
            targetAngleDegrees = Math.round(accumulatedDegrees / STEP_DEGREES) * STEP_DEGREES;
            isMoving = true;
            isSettling = false;
        });
    }

    public double getAccumulatedDegrees() {
        return accumulatedDegrees;
    }

    public double getTargetAngleDegrees() {
        return targetAngleDegrees;
    }

    @Override
    public void periodic() {
        updateAccumulatedAngle();

        if (!isInitialized) {
            servo.setPower(0.0);
            return;
        }

        double error = targetAngleDegrees - accumulatedDegrees;
        double absError = Math.abs(error);
        double elapsedSeconds = (System.nanoTime() - moveStartTimeNanos) / 1e9;

        boolean timeoutTripped = isMoving && (elapsedSeconds > MOVE_TIMEOUT_SECONDS);

        if (timeoutTripped) {
            servo.setPower(0.0);
            // Grid-lock snap to nearest 120-degree step on timeout
            targetAngleDegrees = Math.round(accumulatedDegrees / STEP_DEGREES) * STEP_DEGREES;
            isMoving = false;
            isSettling = false;
        } else if (isMoving) {
            // Check if within primary tolerance window
            if (absError <= ANGLE_TOLERANCE_DEGREES) {
                servo.setPower(0.0);
                isMoving = false;
                isSettling = false;
            } else {
                // Check if inside settling band to start timer guard
                if (absError <= SETTLING_BAND_DEGREES) {
                    if (!isSettling) {
                        settlingStartTimeNanos = System.nanoTime();
                        isSettling = true;
                    }

                    double settlingElapsedSec = (System.nanoTime() - settlingStartTimeNanos) / 1e9;
                    if (settlingElapsedSec > SETTLING_TIMEOUT_SECONDS) {
                        // Settling timer expired - kill power cleanly to stop chatter
                        servo.setPower(0.0);
                        isMoving = false;
                        isSettling = false;
                        return;
                    }
                } else {
                    isSettling = false;
                }

                // Smooth deceleration curve without hard minimum power lock inside final approach
                double pScale = Math.min(1.0, absError / 25.0);
                double calculatedPower = INDEX_POWER * pScale;

                // Only enforce MIN_POWER when outside the settling zone to get moving smoothly
                if (absError > SETTLING_BAND_DEGREES) {
                    calculatedPower = Math.max(MIN_POWER, calculatedPower);
                }

                servo.setPower(Math.signum(error) * calculatedPower);
            }
        } else {
            servo.setPower(0.0);
        }

        if (debugLogsEnabled) {
            Telemetry.log("Indexer Analog Volts", String.format("%.3f V", getRawVoltage()));
            Telemetry.log("Indexer Raw Angle", String.format("%.1f deg", getRawDegrees()));
            Telemetry.log("Indexer Accumulated Deg", String.format("%.1f deg", accumulatedDegrees));
            Telemetry.log("Indexer Target Deg", String.format("%.1f deg", targetAngleDegrees));
            Telemetry.log("Indexer Error Deg", String.format("%.1f deg", error));
            Telemetry.log("Indexer Motor Power", servo.getPower());
        }
    }
}