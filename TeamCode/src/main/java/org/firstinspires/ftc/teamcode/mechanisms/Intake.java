package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class Intake implements Mechanism {
    private final NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), Config.intakeMotor);


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

    public Intake(){
        intakeMotor.getVelocityConstants().setKV(kV);
        intakeMotor.getVelocityConstants().setKS(kS);
        intakeMotor.setDirection(NextMotor.Direction.REVERSE);
    }


    public NextMotor getLauncherMotor(){
        return intakeMotor;
    }

    public void setTargetVelocity(double targetVelocity){
         intakeMotor.setVelocitySetpoint(RotationsPerMinute.of(targetVelocity));
    }

    public Command forward(){
        return instant(()->intakeMotor.setThrottle(1.0));
    }
    public Command reverse(){
        return instant(()->intakeMotor.setThrottle(-1.0));
    }
    public Command stop(){
        return instant(()->intakeMotor.setVelocitySetpoint(RotationsPerMinute.of(0)));
    }




/*

    public Command setPowerThingy() {
        curPower = 0.5;
        return instant(() -> launcherMotor.setThrottle(0.5));
    }

    public Command incrementPower(){
        curPower +=0.01;
        return instant(() -> launcherMotor.setThrottle(curPower + 0.01));
    }

    public Command decrementPower(){
        curPower -=0.01;
        return instant(() -> launcherMotor.setThrottle(curPower - 0.01));
    }
*/


//    public double getMotorSpeed() {
//        return launcherMotor.getThrottle();
//    }

    public boolean isAtVelocity(){
        return Math.abs(currentVelocity - targetVelocity) <= TOLERANCE;
    }

    @Override
    public void periodic() {
        currentVelocity = intakeMotor.getEncoderVelocity().into(RotationsPerMinute);

        isAtVelocity();
    }


}