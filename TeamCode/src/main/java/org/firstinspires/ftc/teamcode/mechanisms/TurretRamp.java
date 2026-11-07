package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;
@Configurable
public class TurretRamp implements Mechanism {
    public TurretRamp(){
    }

    private final NextServo rightServo = new NextServo(RobotController.expansionHub(), Config.rightTurretServo,0.01, NextMotor.Direction.FORWARD);

    private final NextServo leftServo = new NextServo(RobotController.expansionHub(), Config.leftTurretServo, 0.01, NextMotor.Direction.REVERSE);

    public static double startPosition = 0.76;

    public static double currentPosition = startPosition;

    public static boolean debugLogsEnabled = false;


    public Command goHome() {
        return instant(() -> {
            currentPosition = startPosition;
            leftServo.setPosition(currentPosition);
            rightServo.setPosition(currentPosition);
        });
    }

    public Command goForward() {
        return instant(() -> {
            currentPosition = currentPosition + 0.01;
            leftServo.setPosition(currentPosition);
            rightServo.setPosition(currentPosition);
        });
    }
    public Command goBackwards() {
        return instant(() -> {
            currentPosition = currentPosition - 0.01;
            leftServo.setPosition(currentPosition);
            rightServo.setPosition(currentPosition);
        });
    }

    @Override
    public void periodic() {
        if (debugLogsEnabled) {
            Telemetry.log("TurretRamp desired position", currentPosition);
            Telemetry.log("Right TurretRamp Servo position", rightServo.getPosition());
            Telemetry.log("Left TurretRamp Servo position", leftServo.getPosition());
        }
    }


}