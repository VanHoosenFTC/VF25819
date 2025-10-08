package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class DriveMotor implements Subsystem {
    private final MotorEx motor;

    public DriveMotor(String name) {
        this.motor = new MotorEx(name);;
    }

    private final ControlSystem controlSystem  = ControlSystem.builder()
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
}

