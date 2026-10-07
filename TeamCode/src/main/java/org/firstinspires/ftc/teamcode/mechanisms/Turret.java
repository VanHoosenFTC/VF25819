package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.robot.Telemetry.log;
import static dev.nextftc.units.Units.Degrees;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.measuretypes.Angle;

import org.firstinspires.ftc.teamcode.data.Config;

public class Turret implements Mechanism {

    // Hardware specifications for goBILDA 5202/5203 Series 117 RPM Motor (50.9:1 Ratio)
    private static final double MOTOR_TICKS_PER_REV = 1425.1;

    // AndyMark Robits 5.5 in. ID Turntable Assembly (am-5470) gear ratio (200T / 50T = 4.0)
    private static final double TURNTABLE_GEAR_RATIO = 4.0;

    // Final output counts per 360-degree revolution at the turntable
    private static final double TICKS_PER_TURNTABLE_REV = MOTOR_TICKS_PER_REV * TURNTABLE_GEAR_RATIO;

    private static final Angle ANGLE_PER_COUNT = Degrees.of(360.0 / TICKS_PER_TURNTABLE_REV);

    private final NextMotor turretMotor = new NextMotor(
            RobotController.expansionHub(),
            Config.turretMotor,
            ANGLE_PER_COUNT,
            0.01
    );

    private static final Angle HOME_ANGLE = Degrees.of(0);
    private static final Angle LEFT_ANGLE = Degrees.of(-120);
    private static final Angle RIGHT_ANGLE = Degrees.of(120);

    private static final double TOLERANCE_DEGREES = 2.0;

    // Tuning constants
    public double kP = 0.01;
    public double kS = 0.16;
    public double kV = 0.0000571;

    // Lazy-loaded baseline handling
    private boolean isZeroed = false;
    private double zeroOffsetDeg = 0.0;

    public Turret() {
        turretMotor.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);

        turretMotor.getPositionConstants()
                .withP(kP)
                .withS(kS)
                .withV(kV);
    }

    private Command moveTo(Angle targetAngle) {
        return infinite(() -> {
            // Safely capture the physical zero position on the very first execution loop
            if (!isZeroed) {
                zeroOffsetDeg = turretMotor.getEncoderPosition().getMagnitude();
                isZeroed = true;
            }

            // Shift the target so NextMotor's internal PID targets the correct absolute encoder tick
            Angle realTarget = Degrees.of(targetAngle.getMagnitude() + zeroOffsetDeg);
            turretMotor.setPositionSetpoint(realTarget);
            turretMotor.update();

            // Calculate logging math against our relative zero so telemetry makes sense
            double targetDeg = targetAngle.getMagnitude();
            double currentDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
            double errorDeg = targetDeg - currentDeg;

            log("Turret Target (deg)", targetDeg);
            log("Turret Current (deg)", currentDeg);
            log("Turret Error (deg)", errorDeg);
        })
                .until(() -> {
                    if (!isZeroed) return false;

                    double currentDeg = turretMotor.getEncoderPosition().getMagnitude() - zeroOffsetDeg;
                    boolean done = Math.abs(currentDeg - targetAngle.getMagnitude()) <= TOLERANCE_DEGREES;

                    if (done) {
                        // Kills the background PID controller and stops the kS chatter
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
}