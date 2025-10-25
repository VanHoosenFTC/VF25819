package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private double velo;

    private Intake() { }

    private MotorEx motor = new MotorEx("INTAKE");
    

    public Command start = new InstantCommand(() -> {
        velo = -0.9;
    }).requires(this);
    public Command stop = new InstantCommand(() -> {
        velo = 0;
    }).requires(this);
    public Command reverse = new InstantCommand(() -> {
        velo = 0.9;
    }).requires(this);


    @Override
    public void periodic() {

        ActiveOpMode.telemetry().addData("intake motor power", motor.getPower());
        ActiveOpMode.telemetry().addData("intake motor velocity", motor.getVelocity());
        motor.setPower(velo);
    }

    @Override
    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}

