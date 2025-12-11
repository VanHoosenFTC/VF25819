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

    public static final int MAX_VELO = 2080;
    public static final double EJECT_POWER = -1;

    public static int veloFudgeFactor = 350;

    public static double veloTolerance = 0.15;

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

    private double goal;

    private boolean stopped = true;

    public Command start = new InstantCommand(() -> {
        stopped = false;
        goal = MAX_VELO * powerFactor;
        new RunToVelocity(controlSystem, goal).requires(this).schedule();
    }).requires(this);

    public Command warmup = new InstantCommand(() -> {
        stopped = false;
        goal = MAX_VELO * powerFactor;
        new RunToVelocity(controlSystem, goal*LauncherSubsystem.warmUpPercent).requires(this).schedule();
    }).requires(this);

    public Command stop = new InstantCommand(() -> {
        stopped = true;
        goal = 0;
    }).requires(this);

    public Command eject = new InstantCommand(() -> {
        stopped = false;
        goal = MAX_VELO * EJECT_POWER;
        new RunToVelocity(controlSystem, goal).requires(this).schedule();
    }).requires(this);

    public boolean nearGoal() {
        // if the goal +350 is within +/- 15% of the motor.getVelocity then we are near the goal
        return Math.abs(goal + veloFudgeFactor - motor.getVelocity()) < (goal * veloTolerance);
    }


    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Launcher Power Factor", powerFactor);
        ActiveOpMode.telemetry().addData("Launcher goal", goal);
        if (stopped) {
            motor.setPower(0);
        } else {
            motor.setPower(controlSystem.calculate());
        }
        ActiveOpMode.telemetry().addData("Launcher velocity", motor.getVelocity());
        ActiveOpMode.telemetry().addData("Launcher power", motor.getPower());

    }

    public static void setPowerFactor(double percent) {
        powerFactor = percent;
    }
}

