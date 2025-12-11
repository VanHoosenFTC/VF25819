package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Light implements Subsystem {
    public static final Light INSTANCE = new Light();
    public static final double GREEN = 0.5;
    public static final double RED = 0.28;

    public static final double YELLOW = 0.338;

    public static final double ORANGE = 0.333;


    public static final double BLUE = 0.62;
    public static final double OFF = 0;

    private double color = 0.0;

    private final ServoEx servo = new ServoEx("LIGHT");
    private Light() { }

    @Override
    public void periodic() {
        servo.setPosition(color);
    }

    @Override
    public void initialize() {
        servo.setPosition(0);
    }

    public Command setColor(double color) {
        return new InstantCommand(() -> {
            this.color = color;
        }).requires(this);
    }
}
