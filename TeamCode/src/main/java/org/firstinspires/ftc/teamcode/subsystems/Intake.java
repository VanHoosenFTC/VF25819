package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    private MotorEx motor = new MotorEx("INTAKE");

    private ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(0.0005, 0, 0)
            .basicFF(0)
            .build();

    public Command start = new RunToVelocity(controlSystem, 10145).requires(this);
    public Command stop = new RunToVelocity(controlSystem, 0).requires(this);
    public Command reverse = new RunToVelocity(controlSystem, -10145).requires(this);


    @Override
    public void periodic() {
        motor.setPower(controlSystem.calculate(motor.getState()));
    }

    @Override
    public void initialize() {
        motor.reverse();
    }
}

