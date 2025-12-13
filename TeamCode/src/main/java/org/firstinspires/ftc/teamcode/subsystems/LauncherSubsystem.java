package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.ShootingPosition;
import org.firstinspires.ftc.teamcode.auton.AutonConstants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.ActiveOpMode;

@Configurable
public class LauncherSubsystem extends SubsystemGroup {
    public static final LauncherSubsystem INSTANCE = new LauncherSubsystem();

    public static double warmUpPercent = 1;

    private ShootingPosition shootingPosition = ShootingPosition.TOP;

    private  double topPowerFactor = AutonConstants.TopLauncherPercent;
    private  double backPowerFactor = AutonConstants.BackLauncherPercent;

    public static double minPower = 0.4;
    public static double maxPower = 1.0;

    private LauncherSubsystem() {
        super(
                Launcher.INSTANCE,
                Gate.INSTANCE,
                IntakeSubsystem.INSTANCE
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

    public Command warmup = new InstantCommand(() -> {
            double powerFactor = getPower(shootingPosition) * warmUpPercent;
            Launcher.setPowerFactor(powerFactor);
            Launcher.INSTANCE.start.schedule();
        }).requires(this);

    public Command launchContinuous() {
        return new InstantCommand(() -> {
            Launcher.setPowerFactor(getPower(this.shootingPosition));
            new ParallelGroup(
                Launcher.INSTANCE.start,
                IntakeSubsystem.INSTANCE.start,
                Gate.INSTANCE.open
            ).schedule();
        }).requires(this);
    }

    public Command stopLauncherAndIntakeAndCloseGate() {
        return new InstantCommand(() -> {
            new ParallelGroup(
                    Launcher.INSTANCE.stop,
                    IntakeSubsystem.INSTANCE.stop,
                    Gate.INSTANCE.close
            ).schedule();
        }).requires(this);
    }

    public Command stopLauncherAndIdle() {
        return new InstantCommand(() -> {
            new ParallelGroup(
                    Launcher.INSTANCE.stop,
                    IntakeSubsystem.INSTANCE.idle
                    ).schedule();
        }).requires(this);
    }

    public Command stopLauncher() {
        return new InstantCommand(() -> {
            new ParallelGroup(
                    Launcher.INSTANCE.stop
            ).schedule();
        }).requires(this);
    }

    public Command closeGateAndIdle() {
        return new InstantCommand(() -> {
            new ParallelGroup(
                    IntakeSubsystem.INSTANCE.idle,
                    Gate.INSTANCE.close
            ).schedule();
        }).requires(this);
    }
    public Command adjustPowerFactor(double adjustment) {
        return new InstantCommand(() -> {
            backPowerFactor = adjust(backPowerFactor, adjustment);
            topPowerFactor = adjust(topPowerFactor, adjustment);
        }).requires(this);

    }

    private double adjust(double powerFactor, double adjustment) {
        double power = powerFactor;
        if (power + adjustment > maxPower) {
            power = maxPower;
        } else if (power + adjustment < minPower) {
            power = minPower;
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

    public Command setShootingPositionAndWarmup(ShootingPosition shootingPosition) {
        return new InstantCommand(() -> {
            this.shootingPosition = shootingPosition;
            this.warmup.schedule();
        }).requires(this);
    }
}

