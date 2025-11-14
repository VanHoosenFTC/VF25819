package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.ShootingPosition;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.ActiveOpMode;

@Configurable
public class LauncherSubsystem extends SubsystemGroup {
    public static final LauncherSubsystem INSTANCE = new LauncherSubsystem();
    private double launcherWarmUp = 1.6;
    private double scoringDelay = 0.5;

    private double topPowerFactor = 0.73;
    private double backPowerFactor = 0.85;

    private LauncherSubsystem() {
        super(
                Launcher.INSTANCE,
                Lift.INSTANCE
        );
    }

    private double getPower(ShootingPosition shootingPositions) {
        double power = 0.0;
        // switch the powerFactor based on the shooting position
        switch (shootingPositions) {
            case BACK:
                power = backPowerFactor;
                break;
            case TOP:
                power = topPowerFactor;
                break;
        }
        return power;
    }

    public Command launchTwo(ShootingPosition shootingPosition) {
        return new InstantCommand(() -> {
            double power = getPower(shootingPosition);
            Launcher.setPowerFactor(power);
            new SequentialGroup(Launcher.INSTANCE.start)
                    .thenWait(launcherWarmUp)
                    .then(Lift.INSTANCE.score)
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.load)
                    .thenWait(0.5)
                    .then(Intake.INSTANCE.start)
                    .thenWait(0.1)
                    .then(Intake.INSTANCE.stop)
                    .thenWait(0.5)
                    .then(Lift.INSTANCE.score)
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.load)
                    .then(Launcher.INSTANCE.stop).schedule();
        });
    }

    public Command launchTwoRunning =
            new SequentialGroup(Lift.INSTANCE.load)
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.score)
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.load)
                    .thenWait(0.5)
                    .then(Intake.INSTANCE.start)
                    .thenWait(0.1)
                    .then(Intake.INSTANCE.stop)
                    .thenWait(0.5)
                    .then(Lift.INSTANCE.score)
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.load);

    public Command adjustPowerFactor(double adjustment) {
        return new InstantCommand(() -> {
            backPowerFactor = adjust(backPowerFactor, adjustment);
            topPowerFactor = adjust(topPowerFactor, adjustment);
        }).requires(this);

    }

    private double adjust(double powerFactor, double adjustment) {
        double power = powerFactor;
        if (power + adjustment > 1) {
            power = 1;
        } else if (power + adjustment < 0.6) {
            power = 0.6;
        } else {
            power = powerFactor + adjustment;
        }
        ActiveOpMode.telemetry().addData("calculated powerFactor", power);
        return power;
    }

    @Override
    public void periodic() {
        super.periodic();
        ActiveOpMode.telemetry().addData("back powerFactor:", backPowerFactor);
        ActiveOpMode.telemetry().addData("top powerFactor:", topPowerFactor);
    }
}

