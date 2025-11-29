package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToVelocity;
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

    public static ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(0.0005, 0, 0)
            .basicFF(0)
            .build();
    private MotorEx motor = new MotorEx("LAUNCHER");

    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private double velo;

    private boolean stopped = true;

    public Command start = new InstantCommand(() -> {
        stopped = false;
        velo = MAX_VELO * powerFactor;
        new RunToVelocity(controlSystem, 2080*powerFactor).requires(this).schedule();
    }).requires(this);

    public Command warmup = new InstantCommand(() -> {
        stopped = false;
        velo = MAX_VELO * powerFactor;
        new RunToVelocity(controlSystem, (2080*powerFactor)*LauncherSubsystem.warmUpPercent).requires(this).schedule();
    }).requires(this);

    public Command stop = new InstantCommand(() -> {
        stopped = true;
        velo = 0;
    }).requires(this);

    public Command eject = new InstantCommand(() -> {
        stopped = false;
        velo = MAX_VELO * EJECT_POWER;
        new RunToVelocity(controlSystem, 2080*powerFactor).requires(this).schedule();
    }).requires(this);


    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher Velocity", velo);
        if (stopped) {
            motor.setPower(0);
        } else {
            motor.setPower(controlSystem.calculate());
        }

        ActiveOpMode.telemetry().addData("motor power", motor.getPower());
        ActiveOpMode.telemetry().addData("motor velocity", motor.getVelocity());

    }

    public static void setPowerFactor(double percent) {
        powerFactor = percent;
    }
}

