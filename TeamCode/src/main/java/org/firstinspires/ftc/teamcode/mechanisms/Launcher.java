package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.units.measuretypes.Angle;

@Configurable
public class Launcher implements Mechanism {


    // goBILDA 5203 Series 6000 RPM Motor (1:1 Ratio)
    private static final double MOTOR_TICKS_PER_REV = 28.0;
    private static final Angle ANGLE_PER_COUNT = Degrees.of(360.0 / MOTOR_TICKS_PER_REV);
    private final NextMotor launcherMotor = new NextMotor(RobotController.controlHub(), Config.launcherMotor, ANGLE_PER_COUNT, 0.01);
    private final NextServo launcherServo = new NextServo(RobotController.controlHub(), Config.launcherServo, 0);

    // Tuning constants - Made public static so they appear in FTControl Panels
    public static boolean debugLogsEnabled = false;
    public static double TOLERANCE = 25.0;

    public static double POLLEN = 0.59;
    public static double NECTAR = 0.5;

    public static double kP = 0.001; // Added for RPM drop recovery
    public static double kV = 0.0000571;
    public static double kS = 0.16;

    // Converted from a raw 0-1 power value to an actual RPM target
    public static double targetRPM = 3500.00;

    private double currentVelocity = 0.0;
    private double targetVelocity = 0.0;

    // Diagnostic tracking fields
    private int freezeCounter = 0;
    private boolean isSpinningCommanded = false;

    public Launcher() {
        launcherMotor.setDirection(NextMotor.Direction.REVERSE);

        // Initial setup for velocity constants using builder methods
        launcherMotor.getVelocityConstants()
                .withP(kP)
                .withV(kV)
                .withS(kS);
    }

    public Command init() {
        return instant(() -> launcherServo.setPosition(POLLEN));
    }

    public NextMotor getLauncherMotor() {
        return launcherMotor;
    }

    public Command increaseSpeed() {
        // Increment target by 100 RPM
        return instant(() -> targetRPM = targetRPM + 100.0);
    }

    public Command decreaseSpeed() {
        // Decrement target by 100 RPM
        return instant(() -> targetRPM = targetRPM - 100.0);
    }

    public Command setPollen() {
        return instant(() -> launcherServo.setPosition(POLLEN));
    }

    public Command setNectar() {
        return instant(() -> launcherServo.setPosition(NECTAR));
    }

    public Command shoot() {
        return instant(() -> {
            isSpinningCommanded = true;
            targetVelocity = targetRPM; // Sync target for the PID and telemetry graph

            // Uses closed-loop PID control instead of raw open-loop throttle
            launcherMotor.setVelocitySetpoint(RotationsPerMinute.of(targetVelocity));
        });
    }

    public Command stopLauncher() {
        return instant(() -> {
            isSpinningCommanded = false;
            targetVelocity = 0.0;
            launcherMotor.setThrottle(0.0);
            launcherMotor.setVelocitySetpoint(RotationsPerMinute.of(0));
        });
    }

    public boolean isAtVelocity() {
        return Math.abs(currentVelocity - targetVelocity) <= TOLERANCE;
    }

    /**
     * Called automatically every hardware loop.
     * Pushes live Panel updates down to the hardware immediately.
     */
    @Override
    public void periodic() {
        // Continuously apply live tuning updates from FTControl Panels using builder methods
        launcherMotor.getVelocityConstants()
                .withP(kP)
                .withV(kV)
                .withS(kS);

        currentVelocity = launcherMotor.getEncoderVelocity().into(RotationsPerMinute);

        // Run diagnostic health check on encoder signal
        checkEncoderHealth();

        // LOGGING UPDATES FOR GRAPHING
        Telemetry.log("Launcher Target (RPM)", targetVelocity);
        Telemetry.log("Launcher Current (RPM)", currentVelocity);

        isAtVelocity();
    }

    /**
     * Diagnostic check to verify whether the launcher encoder cable is sending feedback
     * while motor output power is commanded.
     */
    private void checkEncoderHealth() {
        // If throttle/power is turned on but velocity stays near 0 RPM, count frozen frames
        if (isSpinningCommanded && Math.abs(currentVelocity) < 1.0) {
            freezeCounter++;
        } else {
            freezeCounter = 0;
        }
        if (debugLogsEnabled) {
            Telemetry.log("Launcher DIAG Velocity", currentVelocity);
            Telemetry.log("Launcher DIAG Frozen Frames", freezeCounter);
            Telemetry.log("Launcher DIAG Encoder Status",
                    (freezeCounter > 25) ? "⚠️ LAUNCHER ENCODER FROZEN / UNPLUGGED" : "OK");
        }
    }
}