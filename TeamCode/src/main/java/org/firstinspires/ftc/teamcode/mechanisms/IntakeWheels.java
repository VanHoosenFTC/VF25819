package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class IntakeWheels implements Mechanism {
    public IntakeWheels(){
    }

    private NextCRServo rightservo = new NextCRServo(RobotController.controlHub(), Config.rightservo);

    private NextCRServo leftservo = new NextCRServo(RobotController.controlHub(), Config.leftservo);


    public Command forward(double power){
        leftservo.setDirection(NextMotor.Direction.REVERSE);
        rightservo.setDirection(NextMotor.Direction.FORWARD);
        return instant(()-> leftservo.setPower(power) leftservo.setPower(power));
    }

    public Command reverse(double power){
        leftservo.setDirection(NextMotor.Direction.FORWARD);
        rightservo.setDirection(NextMotor.Direction.REVERSE);
        return instant(()-> servo.setPower(power));

    }

}