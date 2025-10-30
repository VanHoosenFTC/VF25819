package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.SubsystemGroup;

@Configurable
public class LauncherSubsystem extends SubsystemGroup {
    public static final LauncherSubsystem INSTANCE = new LauncherSubsystem();
    public double launcherWarmUp = 2.0;
    public double scoringDelay = 0.5;

    private LauncherSubsystem() {
        super(
                Launcher.INSTANCE,
                Lift.INSTANCE
        );
    }

    public Command launchOne =
            new SequentialGroup(Launcher.INSTANCE.start
                    .thenWait(launcherWarmUp)
                    .then(Lift.INSTANCE.score))
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.load)
                    .then(Launcher.INSTANCE.stop);
    public Command launchTwo =
            new SequentialGroup(Launcher.INSTANCE.start
                    .thenWait(launcherWarmUp)
                    .then(Lift.INSTANCE.score))
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
                    .then(Launcher.INSTANCE.stop);

}

