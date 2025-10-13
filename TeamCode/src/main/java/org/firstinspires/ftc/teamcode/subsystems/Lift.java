package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;

public class Lift implements Subsystem {
    public static final Lift INSTANCE = new Lift();
    private Lift() { }

    private final ServoEx servo = new ServoEx("LIFT");

    private static final double LOADING_POSITION = 0.6; //start at 0.5 the values go from 0 to 1.  normally

    private static final double SCORING_POSITION = 0;

    private double position = LOADING_POSITION;



    @Override
    public void initialize() {
        servo.setPosition(position);
    }

    public Command load = new InstantCommand(() -> {
        position = LOADING_POSITION;
    }).requires(this);


    public Command score = new InstantCommand(() -> {
        position = SCORING_POSITION;
    }).requires(this);

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Lift Position", position);
        ActiveOpMode.telemetry().update();
        servo.setPosition(position);
    }

}
