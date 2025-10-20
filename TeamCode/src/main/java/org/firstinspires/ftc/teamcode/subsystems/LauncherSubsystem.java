package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.SubsystemGroup;

@Configurable
public class LauncherSubsystem extends SubsystemGroup {
    public static final LauncherSubsystem INSTANCE = new LauncherSubsystem();
    public static double launcherWarmUp = 2.0;
    public static double scoringDelay = 1.0;

    private LauncherSubsystem() {
        super(
                Launcher.INSTANCE,
                Lift.INSTANCE
        );
    }

    public Command launch =
            new SequentialGroup(Launcher.INSTANCE.start
                    .thenWait(launcherWarmUp)
                    .then(Lift.INSTANCE.score))
                    .thenWait(scoringDelay)
                    .then(Lift.INSTANCE.load)
                    .then(Launcher.INSTANCE.stop);

}

