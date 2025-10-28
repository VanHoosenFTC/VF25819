package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
@Configurable
public class Lift implements Subsystem {
    public static final Lift INSTANCE = new Lift();
    private Lift() { }

    private final ServoEx servo = new ServoEx("LIFT");

    private double loading = 0.54;

    private double scoring = 0;

    private double position = loading;



    @Override
    public void initialize() {
        ActiveOpMode.telemetry().addData("Initializing Lift: Position - ", position);
        servo.setPosition(position);
    }

    public Command load = new InstantCommand(() -> {
        position = loading;
    }).requires(this);


    public Command score = new InstantCommand(() -> {
        position = scoring;
    }).requires(this);

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Lift Position", position);
        servo.setPosition(position);
    }

    public Command preLoad = new InstantCommand(() -> {
        servo.setPosition(loading - .01);
    }).requires(this);

}
