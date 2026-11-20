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
public class SecondaryIntake implements Mechanism {

    private static boolean debugLogsEnabled = false;

    // Live configurable running motor power when active (updates instantly in Panels)
    public static double POWER = 0.9;

    // Live configurable approach power when seeking target position
    public static double HOMING_POWER = 0.40;

    // Rotation angle increment on stop (degrees)
    public static double ROTATION_STEP_DEGREES = 180.0;

    // Tolerance band around target position (degrees)
    public static double TARGET_TOLERANCE_DEGREES = 4.0;

    // Settling band to start timeout guard (degrees)
    public static double SETTLING_BAND_DEGREES = 8.0;

    // Maximum allowed time to settle before forcing power off (seconds)
    public static double HOMING_TIMEOUT_SECONDS = 1.0;

    // Flips encoder direction matching physical servo rotation
    public static boolean INVERT_ENCODER = true;

    // Hardware devices explicitly assigned to the Expansion Hub
    private final NextCRServo servo = new NextCRServo(RobotController.expansionHub(), Config.secondaryIntakeServo);
    private final NextAnalogInput encoder = new NextAnalogInput(RobotController.expansionHub(), Config.secondaryIntakeEncoder);

    private double targetAngleDegrees = 0.0;
    private boolean isHoming = false;
    private boolean isRunningForward = false;
    private boolean isRunningReverse = false;
    private boolean isInitialized = false;

    private long homingStartTimeNanos = 0;
    private long settlingStartTimeNanos = 0;
    private boolean isSettling = false;

    public SecondaryIntake() {
        // Safe lazy hardware instantiation
    }

    /**
     * Reads raw 0-360 degree position from the analog feedback wire.
     */
    public double getRawDegrees() {
        double raw = encoder.getValue() * 360.0;
        return INVERT_ENCODER ? (360.0 - raw) % 360.0 : raw;
    }

    public double getRawVoltage() {
        return encoder.getRawVoltage().into(Volts);
    }

    /**
     * Resets the target position to match current physical position.
     */
    public void resetTarget() {
        targetAngleDegrees = getRawDegrees();
        isHoming = false;
        isRunningForward = false;
        isRunningReverse = false;
        isSettling = false;
        servo.setPower(0.0);
    }

    public Command forward() {
        return instant(() -> {
            isHoming = false;
            isRunningReverse = false;
            isRunningForward = true;
        });
    }

    public Command reverse() {
        return instant(() -> {
            isHoming = false;
            isRunningForward = false;
            isRunningReverse = true;
        });
    }

    /**
     * Initiates a 180-degree rotation from current position and aligns to the new target.
     */
    public Command stop() {
        return instant(() -> {
            isRunningForward = false;
            isRunningReverse = false;

            double current = getRawDegrees();
            // Calculate 180-degree offset wrapped within [0, 360)
            targetAngleDegrees = (current + ROTATION_STEP_DEGREES) % 360.0;
            isHoming = true;
            isSettling = false;
            homingStartTimeNanos = System.nanoTime();
        });
    }

    @Override
    public void periodic() {
        double currentRaw = getRawDegrees();

        // Capture initial position on first periodic loop & set idle
        if (!isInitialized) {
            targetAngleDegrees = currentRaw;
            isInitialized = true;
            servo.setPower(0.0);
            return;
        }

        // Continually apply live POWER variable during active manual runs so Panels slider adjusts in real time
        if (isRunningForward) {
            servo.setPower(POWER);
        } else if (isRunningReverse) {
            servo.setPower(-POWER);
        } else if (isHoming) {
            // Shortest path error calculation on 360-degree continuous circle
            double error = targetAngleDegrees - currentRaw;
            if (error > 180.0) {
                error -= 360.0;
            } else if (error < -180.0) {
                error += 360.0;
            }

            double absError = Math.abs(error);
            double elapsedHomingSec = (System.nanoTime() - homingStartTimeNanos) / 1e9;

            // Global safety timeout guard
            if (elapsedHomingSec > HOMING_TIMEOUT_SECONDS) {
                servo.setPower(0.0);
                isHoming = false;
                isSettling = false;
            } else if (absError <= TARGET_TOLERANCE_DEGREES) {
                // Reached target cleanly inside tolerance window
                servo.setPower(0.0);
                isHoming = false;
                isSettling = false;
            } else {
                // Settling timer guard when approaching target
                if (absError <= SETTLING_BAND_DEGREES) {
                    if (!isSettling) {
                        settlingStartTimeNanos = System.nanoTime();
                        isSettling = true;
                    }

                    double settlingElapsedSec = (System.nanoTime() - settlingStartTimeNanos) / 1e9;
                    if (settlingElapsedSec > 0.15) {
                        servo.setPower(0.0);
                        isHoming = false;
                        isSettling = false;
                        return;
                    }
                } else {
                    isSettling = false;
                }

                // Drive to 180-degree offset target with smooth deceleration near arrival
                double pScale = Math.min(1.0, absError / 25.0);
                double drivePower = Math.signum(error) * Math.max(0.18, HOMING_POWER * pScale);
                servo.setPower(drivePower);
            }
        } else {
            // Default idle state
            servo.setPower(0.0);
        }

        if (debugLogsEnabled) {
            Telemetry.log("Secondary Intake Volts", String.format("%.3f V", getRawVoltage()));
            Telemetry.log("Secondary Intake Angle", String.format("%.1f deg", currentRaw));
            Telemetry.log("Secondary Intake 180 Target", String.format("%.1f deg", targetAngleDegrees));
            Telemetry.log("Secondary Intake Power", servo.getPower());
        }
    }
}