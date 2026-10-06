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

    private NextCRServo rightServo = new NextCRServo(RobotController.controlHub(), Config.rightServo);

    private NextCRServo leftServo = new NextCRServo(RobotController.controlHub(), Config.leftServo);


    public Command forward(double power){
        leftServo.setDirection(NextMotor.Direction.REVERSE);
        rightServo.setDirection(NextMotor.Direction.FORWARD);
        return instant(()-> leftServo.setPower(power));

//        return instant(new Runnable() {
//            @Override
//            public void run() {
//                leftServo.setPower(power);
//                rightServo.setPower(power);
//            }
//        });
    }

    public Command reverse(double power){
        leftServo.setDirection(NextMotor.Direction.FORWARD);
        rightServo.setDirection(NextMotor.Direction.REVERSE);
        return instant(()-> leftServo.setPower(power));

    }

}