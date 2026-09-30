package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class ContinuousServoExample implements Mechanism {
    public ContinuousServoExample(){
    }

    private NextCRServo servo = new NextCRServo(RobotController.controlHub(), Config.continuousServoExample);


    public Command forward(double power){
        servo.setDirection(NextMotor.Direction.FORWARD);
        return instant(()-> servo.setPower(power));
    }

    public Command reverse(double power){
        servo.setDirection(NextMotor.Direction.REVERSE);
        return instant(()-> servo.setPower(power));
    }

}