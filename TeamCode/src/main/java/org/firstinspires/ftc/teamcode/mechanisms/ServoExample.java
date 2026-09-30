package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class ServoExample implements Mechanism {
    public ServoExample(){
    }

    private final double OPEN_POS = 0.0;

    private final double CLOSE_POS = 1.0;
    private final double MID_POS = CLOSE_POS / 2;

    private NextServo servo = new NextServo(RobotController.controlHub(), Config.servoExample);

    public NextServo getServo() {
        return servo;
    }

    public Command deltaUp(){
        return instant(() -> servo.setPosition(servo.getPosition() + 0.01));
    }

    public Command deltaDown(){
        return instant(() -> servo.setPosition(servo.getPosition() - 0.01));
    }


    private void setRampServoPosition(double x){
        servo.setPosition(x);
    }

    public Command setPosition(double pos){
        return instant(()->setRampServoPosition(pos)).requiring(servo);
    }

    public Command open(){
        return setPosition(OPEN_POS);
    }

    public Command close(){
        return setPosition(CLOSE_POS);
    }

    public Command mid(){return setPosition(MID_POS);}

}