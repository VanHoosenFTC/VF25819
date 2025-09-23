package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Launcher implements Subsystem {
    public static final Launcher INSTANCE = new Launcher();
    private Launcher() { }

    private MotorEx motor = new MotorEx("INTAKE");

    private ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(0.0005, 0, 0)
            .basicFF(0)
            .build();

    public Command start = new RunToVelocity(controlSystem, 10145).requires(this);
    public Command stop = new RunToVelocity(controlSystem, 0).requires(this);

    @Override
    public void periodic() {
        motor.setPower(controlSystem.calculate(motor.getState()));
    }
}

