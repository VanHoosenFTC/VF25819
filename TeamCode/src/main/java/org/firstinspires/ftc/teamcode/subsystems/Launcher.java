package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Launcher implements Subsystem {
    public static final Launcher INSTANCE = new Launcher();
    private Launcher() { }
    private double powerFactor=0.75;
    private MotorEx motor = new MotorEx("LAUNCHER");

    private ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(0.0005, 0, 0)
            .basicFF(0)
            .build();
    //powerfactor 0-1, rightbumper=increase by 0.1, leftbumper=decrease 0.1
    public Command start = new RunToVelocity(controlSystem, 10145*powerFactor).requires(this);
    public Command stop = new RunToVelocity(controlSystem, 0).requires(this);

    public Command adjustPowerFactor(double adjustment) {
        powerFactor = powerFactor + adjustment;
        return new RunToVelocity(controlSystem, 10145* (powerFactor));
    }
    @Override
    public void periodic() {
        motor.setPower(controlSystem.calculate(motor.getState()));
    }
}

