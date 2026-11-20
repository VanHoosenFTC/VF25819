package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Configurable
public class FlowerPicker implements Mechanism {
    public FlowerPicker(){
    }
    public static boolean debugLogsEnabled = false;
    private static double up = 1.0;

    private static double down = 0.23;

    private NextServo servo = new NextServo(RobotController.expansionHub(), Config.flowerPickerServo);
    private void setRampServoPosition(double x){
        servo.setPosition(x);
    }
    public Command setPosition(double pos){
        return instant(()->setRampServoPosition(pos)).requiring(servo);
    }
    public Command up(){
        return setPosition(up);
    }
    public Command down(){
        return setPosition(down);
    }

    @Override
    public void periodic() {
        if (debugLogsEnabled) {
            Telemetry.log("Flower Picker Servo position", servo.getPosition());
        }
    }
}