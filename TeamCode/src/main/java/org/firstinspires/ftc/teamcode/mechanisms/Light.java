package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class Light implements Mechanism {
    public static final double GREEN = 0.5;
    public static final double RED = 0.28;

    public static final double YELLOW = 0.338;

    public static final double ORANGE = 0.333;


    public static final double BLUE = 0.62;
    public static final double OFF = 0;

    private double color;

    private final NextServo servo = new NextServo(RobotController.expansionHub(), Config.light);
    public Light() {
    }

    @Override
    public void periodic() {
        servo.setPosition(color);
    }

    public Command setColor(double color) {
        return instant(() -> this.color = color);
    }
}