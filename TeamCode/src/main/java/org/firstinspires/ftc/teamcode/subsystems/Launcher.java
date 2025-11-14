package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
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
    public static final double EJECT_POWER = -0.8;
    // public static final int MAX_VELO = 10145;

    public static PIDCoefficients pidCoefficients = new PIDCoefficients(0.00023, 0, 0);

    private static ControlSystem controlSystem  = ControlSystem.builder()
            .velPid(pidCoefficients)
            .basicFF(0)
            .build();

    private Launcher() {
    }

    private static double powerFactor = 0.70;

    private static double max_goal = 2000;
    private MotorEx motor = new MotorEx("LAUNCHER").reversed();

    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        controlSystem.setGoal(new KineticState());
    }

    public Command start = new InstantCommand(() -> {
        new RunToVelocity(controlSystem, max_goal * powerFactor).requires(this).schedule();
    }).requires(this);

    public Command stop = new InstantCommand(() -> {
        new RunToVelocity(controlSystem, 0).requires(this).schedule();
    }).requires(this);

    public Command eject = new InstantCommand(() -> {
        new RunToVelocity(controlSystem, -1 * max_goal * EJECT_POWER).requires(this).schedule();
    }).requires(this);

    public Command reverse() {
        return new InstantCommand(() -> {
            motor.reverse();
        }).requires(this);
    }

    @Override
    public void periodic() {
        double powerTarget = controlSystem.calculate(motor.getState());
        ActiveOpMode.telemetry().addData("motor power set to: ", powerTarget);
        motor.setPower(powerTarget);
        ActiveOpMode.telemetry().addData("motor power", motor.getPower());
        ActiveOpMode.telemetry().addData("motor velocity", motor.getVelocity());

    }

    public static void setPowerFactor(double percent) {
        powerFactor = percent;
    }
}

