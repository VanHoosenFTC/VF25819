package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

public class Launcher implements Subsystem {
    public static final Launcher INSTANCE = new Launcher();

    public static final int MAX_VELO = 1;
    // public static final int MAX_VELO = 10145;

    private Launcher() { }
    private double powerFactor=0.5;
    private MotorEx motor = new MotorEx("LAUNCHER");

    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private double velo;




    //powerfactor 0-1, rightbumper=increase by 0.1, leftbumper=decrease 0.1
    public Command start = new InstantCommand(() -> {
        velo = MAX_VELO*powerFactor;
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);
        ActiveOpMode.telemetry().update();
    }).requires(this);

    public Command stop = new InstantCommand(() -> {
        velo = 0;
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);
        ActiveOpMode.telemetry().update();
    }).requires(this);

    public Command adjustPowerFactor(double adjustment) {
        return new InstantCommand(() -> {powerFactor = powerFactor + adjustment;}).requires(this);
    }
    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);


        motor.setPower(velo);
        ActiveOpMode.telemetry().addData("motor power", motor.getPower());
        ActiveOpMode.telemetry().addData("motor velocity", motor.getVelocity());

    }
}

