package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.robot.Telemetry.log;
import static dev.nextftc.units.Units.Degrees;

// Use the specific FTControl Panels annotation

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.units.measuretypes.Angle;

import org.firstinspires.ftc.teamcode.data.Config;

@Configurable
public class Turret implements Mechanism {

    // Hardware specifications for goBILDA 5202/5203 Series 117 RPM Motor (50.9:1 Ratio)
    private static final double MOTOR_TICKS_PER_REV = 1425.1;

    // AndyMark Robits 5.5 in. ID Turntable Assembly (am-5470) gear ratio (200T / 50T = 4.0)
    private static final double TURNTABLE_GEAR_RATIO = 4.0;

    // Final output counts per 360-degree revolution at the turntable
    private static final double TICKS_PER_TURNTABLE_REV = MOTOR_TICKS_PER_REV * TURNTABLE_GEAR_RATIO;

    private static final Angle ANGLE_PER_COUNT = Degrees.of(360.0 / TICKS_PER_TURNTABLE_REV);

    // Motor hardware instantiated on the Control Hub
    private final NextMotor turretMotor = new NextMotor(
            RobotController.controlHub(),
            Config.turretMotor,
            ANGLE_PER_COUNT,
            0.01
    );

    private static final Angle HOME_ANGLE = Degrees.of(0);
    private static final Angle LEFT_ANGLE = Degrees.of(-120);
    private static final Angle RIGHT_ANGLE = Degrees.of(120);

    private static final double TOLERANCE_DEGREES = 2.0;

    public static boolean debugLogsEnabled = false;

    // Tuning constants - Made public static so they appear in FTControl Panels
    public static double kP = 0.01;
    public static double kS = 0.05;
    public static double kV = 0.0000571;

    // Lazy-loaded baseline handling & state tracking
    private boolean isZeroed = false;
    private double zeroOffsetDeg = 0.0;
    private double currentTargetDeg = 0.0;
    private final double MAX_SPEED_DEG_PER_SEC = 360.0; // Increased for snappy manual control
    private final Angle MIN_TURRET_ANGLE_DEG = LEFT_ANGLE;
    private final Angle MAX_TURRET_ANGLE_DEG = RIGHT_ANGLE;

    // Diagnostic tracking fields
    private double lastRawEncoderPos = Double.NaN;
    private int freezeCounter = 0;

    private CommandGamepad gamepad;

    public Turret() {
        turretMotor.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);

        // Initial setup
        turretMotor.getPositionConstants()
                .withP(kP)
                .withS(kS)
                .withV(kV);
    }

    /**
     * Called automatically every hardware loop by the NextFTC/Ivy Scheduler.
     * This pushes live Panel updates down to the hardware immediately.
     */
    @Override
    public void periodic() {
        turretMotor.getPositionConstants()
                .withP(kP)
                .withS(kS)
                .withV(kV);
    }

    /**
     * Links the TeleOp gamepad to the subsystem for manual control.
     */
    public void setGamepad(CommandGamepad gamepad) {
        this.gamepad = gamepad;
    }

    @Override
    public Command getDefaultCommand() {
        // Return the command immediately so the Ivy scheduler can register it during robot init.
        return manualTurretControl();
    }

    private Command moveTo(Angle targetAngle) {
        return infinite(() -> {
            // Safely capture physical zero position on first execution loop
            if (!isZeroed) {
                zeroOffsetDeg = turretMotor.getEncoderPosition().getMagnitude();
                isZeroed = true;
            }

            // Continuously sync currentTargetDeg so manual control picks up seamlessly when this finishes
            currentTargetDeg = targetAngle.getMagnitude();

            // Shift target so NextMotor's internal PID targets the correct absolute encoder tick
            Angle realTarget = Degrees.of(currentTargetDeg + zeroOffsetDeg);
            turretMotor.setPositionSetpoint(realTarget);
            turretMotor.update();

            checkEncoderHealth();

            // Telemetry calculations relative to zero
            double currentDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
            double errorDeg = currentTargetDeg - currentDeg;
            if (debugLogsEnabled) {
                log("Turret Target (deg)", currentTargetDeg);
                log("Turret Current (deg)", currentDeg);
                log("Turret Error (deg)", errorDeg);
            }
        })
                .until(() -> {
                    if (!isZeroed) return false;

                    double currentDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
                    boolean done = Math.abs(currentDeg - targetAngle.getMagnitude()) <= TOLERANCE_DEGREES;

                    if (done) {
                        turretMotor.setThrottle(0.0);
                    }

                    return done;
                });
    }

    public Command goHome() {
        return moveTo(HOME_ANGLE);
    }

    public Command goLeft() {
        return moveTo(LEFT_ANGLE);
    }

    public Command goRight() {
        return moveTo(RIGHT_ANGLE);
    }

    public NextMotor getTurretMotor() {
        return turretMotor;
    }

    public Command manualTurretControl() {
        return infinite(() -> {
            // Failsafe: Do nothing until Teleop.start() passes the gamepad
            if (gamepad == null) return;

            // Safely capture physical zero position on first execution loop
            if (!isZeroed) {
                zeroOffsetDeg = turretMotor.getEncoderPosition().getMagnitude();
                currentTargetDeg = 0.0;
                isZeroed = true;
            }

            // Read right stick X value (-1.0 to 1.0)
            double stickX = gamepad.rightStickX().getValue();

            // Apply deadband
            if (Math.abs(stickX) < 0.05) {
                stickX = 0.0;
            }

            // Update setpoint using loop delta time (~20 ms / 50 Hz loop)
            double dt = 0.02;
            currentTargetDeg += stickX * MAX_SPEED_DEG_PER_SEC * dt;

            // Enforce soft limits
            currentTargetDeg = Math.max(
                    MIN_TURRET_ANGLE_DEG.getMagnitude(),
                    Math.min(MAX_TURRET_ANGLE_DEG.getMagnitude(), currentTargetDeg)
            );

            // Telemetry relative to captured zero
            double currentDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
            double errorDeg = currentTargetDeg - currentDeg;

            // Jitter Fix: Stop motor output when driver releases stick AND turret reaches target
            if (stickX == 0.0 && Math.abs(errorDeg) <= TOLERANCE_DEGREES) {
                turretMotor.setThrottle(0.0);
            } else {
                // Shift target angle relative to zeroOffsetDeg and run PID
                Angle realTarget = Degrees.of(currentTargetDeg + zeroOffsetDeg);
                turretMotor.setPositionSetpoint(realTarget);
            }

            turretMotor.update();

            checkEncoderHealth();
            if (debugLogsEnabled) {
                log("Turret Stick Input", stickX);
                log("Turret Target (deg)", currentTargetDeg);
                log("Turret Current (deg)", currentDeg);
                log("Turret Error (deg)", errorDeg);
            }
        })
                .setStart(() -> {
                    // Sync currentTargetDeg to actual motor position whenever this command resumes from an interruption
                    if (isZeroed) {
                        currentTargetDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
                    }
                })
                .setEnd(interrupted -> {
                    turretMotor.setThrottle(0.0);
                });
    }

    private void checkEncoderHealth() {
        double rawPos = turretMotor.getEncoderPosition().getMagnitude();
        if (debugLogsEnabled) {
            log("DIAG: Raw Encoder Pos", rawPos);
            log("DIAG: Zero Offset (deg)", zeroOffsetDeg);
        }
        if (!Double.isNaN(lastRawEncoderPos)) {
            double delta = Math.abs(rawPos - lastRawEncoderPos);

            // If motor is moving but encoder delta is 0, count frozen frames
            if (delta < 0.0001) {
                freezeCounter++;
            } else {
                freezeCounter = 0;
            }
        }

        lastRawEncoderPos = rawPos;
        if (debugLogsEnabled) {
            // Log warnings to Telemetry if encoder appears disconnected or unresponsive
            log("DIAG: Encoder Delta", lastRawEncoderPos);
            log("DIAG: Frozen Frame Count", freezeCounter);
            log("DIAG: Encoder Status", freezeCounter > 25 ? "⚠️ ENCODER FROZEN / DISCONNECTED" : "OK");
        }
    }

    /**
     * Actively tracks the chosen AprilTag by updating the positional setpoint directly.
     */
    public Command trackTarget(Vision vision) {
        return infinite(() -> {
            Double tx = vision.getTargetTx();

            if (tx != null && isZeroed) {
                double currentDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
                currentTargetDeg = currentDeg + tx;

                // Enforce soft limits
                currentTargetDeg = Math.max(
                        MIN_TURRET_ANGLE_DEG.getMagnitude(),
                        Math.min(MAX_TURRET_ANGLE_DEG.getMagnitude(), currentTargetDeg)
                );

                Angle realTarget = Degrees.of(currentTargetDeg + zeroOffsetDeg);
                turretMotor.setPositionSetpoint(realTarget);
                turretMotor.update();
            }
        });
    }
}