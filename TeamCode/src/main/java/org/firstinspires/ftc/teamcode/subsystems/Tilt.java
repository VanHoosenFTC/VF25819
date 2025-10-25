package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;

public class Tilt implements Subsystem {
    public static final Tilt INSTANCE = new Tilt();
    private Tilt() { }

    private final ServoEx rightServo = new ServoEx("TILT-R");
    private final ServoEx leftServo = new ServoEx("TILT-L");

    private double rightPosition = 0.48; //start at 0.48 the values go from 0 to 1.  normally we would start at 0.5, but our alignment was slightly off when installing the servo

    private double leftPosition = 0.52; //start at 0.52 the values go from 0 to 1.  normally we would start at 0.5, but our alignment was slightly off when installing the servo
    public Command adjust(double adjustment) {
        return new InstantCommand(() -> {
            // subtract value on right side - if we add left side adjustment should be added since it's a mirror of right
            rightPosition = rightPosition - adjustment;
            leftPosition = leftPosition + adjustment;
        }).requires(this);
    }


    @Override
    public void initialize() {
        rightServo.setPosition(rightPosition);
        leftServo.setPosition(leftPosition);
    }

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Right Tilt Position", rightPosition);
        ActiveOpMode.telemetry().addData("Left Tilt Position", leftPosition);
        rightServo.setPosition(rightPosition);
        leftServo.setPosition(leftPosition);
    }


}
