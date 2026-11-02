package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Launcher implements Mechanism {
    private final NextMotor launcherMotor = new NextMotor(RobotController.controlHub(), Config.launcherMotor);
    private final NextServo launcherServo = new NextServo(RobotController.controlHub(), Config.launcherServo, 0);

    private final double TOLERANCE = 25;
    private final double DELTA = 50.0;
    private final double POINT1 = 16000;

    private final double POLLEN = 0.59;
    private final double NECTAR = 0.5;

    public double kV = 0.0000571;
    public double kS = 0.16;

    private double curPower = 0.5;
    private double currentVelocity = 0.0;
    private double targetVelocity = 0.0;

    private double speed = 0.9;

    // Diagnostic tracking fields
    private int freezeCounter = 0;
    private boolean isSpinningCommanded = false;

    public Launcher() {
//        launcherMotor.getVelocityConstants().setKV(kV);
//        launcherMotor.getVelocityConstants().setKS(kS);
        launcherMotor.setDirection(NextMotor.Direction.REVERSE);
    }

    public Command init() {
        return instant(() -> launcherServo.setPosition(POLLEN));
    }

    public NextMotor getLauncherMotor() {
        return launcherMotor;
    }

    public Command increaseSpeed() {
        return instant(() -> speed = speed + 0.05);
    }

    public Command decreaseSpeed() {
        return instant(() -> speed = speed - 0.05);
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
            launcherMotor.setThrottle(speed);
        });
    }

    public Command stopLauncher() {
        return instant(() -> {
            isSpinningCommanded = false;
            launcherMotor.setThrottle(0.0);
            launcherMotor.setVelocitySetpoint(RotationsPerMinute.of(0));
        });
    }

    public boolean isAtVelocity() {
        return Math.abs(currentVelocity - targetVelocity) <= TOLERANCE;
    }

    @Override
    public void periodic() {
        currentVelocity = launcherMotor.getEncoderVelocity().into(RotationsPerMinute);

        // Run diagnostic health check on encoder signal
        checkEncoderHealth();

        Telemetry.log("Launcher Power", speed);
        Telemetry.log("Launcher Velocity (RPM)", currentVelocity);

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

        Telemetry.log("Launcher DIAG Velocity", currentVelocity);
        Telemetry.log("Launcher DIAG Frozen Frames", freezeCounter);
        Telemetry.log("Launcher DIAG Encoder Status",
                (freezeCounter > 25) ? "⚠️ LAUNCHER ENCODER FROZEN / UNPLUGGED" : "OK");
    }
}