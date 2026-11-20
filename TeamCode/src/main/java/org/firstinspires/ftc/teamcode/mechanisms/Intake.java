package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.RotationsPerMinute;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Configurable
public class Intake implements Mechanism {

    private static boolean debugLogsEnabled = false;

    // Configurable throttle power levels for Panels tuning
    public static double FORWARD_POWER = 1.0;
    public static double REVERSE_POWER = -1.0;
    public static double IDLE_POWER = 1.0;

    // Configurable Feedforward constants
    public static double kV = 0.0000571;
    public static double kS = 0.16;

    public static double TOLERANCE = 25.0;

    private final NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), Config.intakeMotor);

    private double currentVelocity = 0.0;
    private double targetVelocity = 0.0;

    private boolean isRunningForward = false;
    private boolean isRunningReverse = false;
    private boolean isIdle = false;

    public Intake() {
        intakeMotor.getVelocityConstants().setKV(kV);
        intakeMotor.getVelocityConstants().setKS(kS);
        intakeMotor.setDirection(NextMotor.Direction.REVERSE);
    }

    public NextMotor getIntakeMotor() {
        return intakeMotor;
    }

    public void setTargetVelocity(double targetVelocity) {
        this.targetVelocity = targetVelocity;
        intakeMotor.setVelocitySetpoint(RotationsPerMinute.of(targetVelocity));
    }

    public Command forward() {
        return instant(() -> {
            isRunningReverse = false;
            isIdle = false;
            isRunningForward = true;
        });
    }

    public Command reverse() {
        return instant(() -> {
            isRunningForward = false;
            isIdle = false;
            isRunningReverse = true;
        });
    }

    public Command idle() {
        return instant(() -> {
            isRunningForward = false;
            isRunningReverse = false;
            isIdle = true;
        });
    }

    public Command stop() {
        return instant(() -> {
            isRunningForward = false;
            isRunningReverse = false;
            isIdle = false;
            targetVelocity = 0.0;
            intakeMotor.setThrottle(0.0);
        });
    }

    public boolean isAtVelocity() {
        return Math.abs(currentVelocity - targetVelocity) <= TOLERANCE;
    }

    @Override
    public void periodic() {
        currentVelocity = intakeMotor.getEncoderVelocity().into(RotationsPerMinute);

        // Update velocity feedforward constants live if tuned in Panels
        intakeMotor.getVelocityConstants().setKV(kV);
        intakeMotor.getVelocityConstants().setKS(kS);

        // Continuously set throttle so Panels slider tweaks apply immediately
        if (isRunningForward) {
            intakeMotor.setThrottle(FORWARD_POWER);
        } else if (isRunningReverse) {
            intakeMotor.setThrottle(REVERSE_POWER);
        } else if (isIdle) {
            intakeMotor.setThrottle(IDLE_POWER);
        }

        if (debugLogsEnabled) {
            Telemetry.log("Intake RPM", String.format("%.1f", currentVelocity));
            Telemetry.log("Intake Throttle", intakeMotor.getThrottle());
        }
    }
}