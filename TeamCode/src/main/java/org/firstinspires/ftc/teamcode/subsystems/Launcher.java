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
    // public static final int MAX_VELO = 10145;

    private Launcher() { }
    private static double powerFactor=0.70;
    private MotorEx motor = new MotorEx("LAUNCHER");

    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.reverse();
    }

    private double velo;

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
        return new InstantCommand(() -> {
            if (powerFactor + adjustment > 1) {
                powerFactor = 1;
            } else if (powerFactor + adjustment < 0.6) {
                powerFactor = 0.6;
            } else {
                powerFactor = powerFactor + adjustment;
            }
        }).requires(this);
    }

    public Command reverse() {
        return new InstantCommand(() ->{
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

