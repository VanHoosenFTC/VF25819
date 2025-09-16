package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    private MotorEx motor = new MotorEx("INTAKE");

    private ControlSystem controlSystem = ControlSystem.builder()
            .posPid(0.005, 0, 0)
            .elevatorFF(0)
            .build();

    public Command start = new RunToPosition(controlSystem, 0).requires(this);
    public Command stop = new RunToPosition(controlSystem, 500).requires(this);
    public Command reverse = new RunToPosition(controlSystem, 1200).requires(this);

    @Override
    public void periodic() {
        motor.setPower(controlSystem.calculate(motor.getState()));
    }
}
