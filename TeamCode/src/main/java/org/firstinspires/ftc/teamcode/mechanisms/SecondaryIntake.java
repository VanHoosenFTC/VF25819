package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Configurable
public class SecondaryIntake implements Mechanism {

    private static boolean debugLogsEnabled = false;

    private static double power = 0.9;
    public SecondaryIntake(){
    }

    private NextCRServo servo = new NextCRServo(RobotController.expansionHub(), Config.secondaryIntakeServo);


    public Command forward(){
        servo.setDirection(NextMotor.Direction.FORWARD);
        return instant(()-> servo.setPower(power));
    }

    public Command reverse(){
        return instant(()-> servo.setPower(-power));
    }

    public Command stop(){
        return instant(()-> servo.setPower(0));
    }

    @Override
    public void periodic() {
        if (debugLogsEnabled) {
            Telemetry.log("Secondary Intake Servo speed", servo.getPower());
        }
    }
}