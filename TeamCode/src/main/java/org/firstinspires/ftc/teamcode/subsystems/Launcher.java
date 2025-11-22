package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class Launcher implements Subsystem {
    public static final Launcher INSTANCE = new Launcher();

    public static final int MAX_VELO = 1;
    public static final double EJECT_POWER = -MAX_VELO;
    // public static final int MAX_VELO = 10145;

    private Launcher() {
    }

    private static double powerFactor = 0.70;
    private MotorEx motor = new MotorEx("LAUNCHER");

    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private double velo;

    public Command start = new InstantCommand(() -> {
        velo = MAX_VELO * powerFactor;
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);
    }).requires(this);

    public Command stop = new InstantCommand(() -> {
        velo = 0;
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);
    }).requires(this);

    public Command eject = new InstantCommand(() -> {
        velo = MAX_VELO * EJECT_POWER;
        ActiveOpMode.telemetry().addData("Launcher Power Factor", EJECT_POWER);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);
    }).requires(this);

    public Command reverse() {
        return new InstantCommand(() -> {
            motor.reverse();
        }).requires(this);
    }

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);

        motor.setPower(velo);
        ActiveOpMode.telemetry().addData("motor power", motor.getPower());
        ActiveOpMode.telemetry().addData("motor velocity", motor.getVelocity());

    }

    public static void setPowerFactor(double percent) {
        powerFactor = percent;
    }
}

