package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Light implements Subsystem {
    public static final Light INSTANCE = new Light();

    private double brightness = 0.0;

    private final ServoEx servo = new ServoEx("LIGHT");
    private Light() { }

    @Override
    public void periodic() {
        servo.setPosition(brightness);
        // nothing to do here
    }

    public Command signal(double brightness) {
        return new InstantCommand(() -> {
            this.brightness = brightness;
        }).requires(this);
    }
}
