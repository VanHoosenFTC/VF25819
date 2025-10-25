package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.ActiveOpMode;



    public class LauncherSubsystems extends SubsystemGroup {
        public static final LauncherSubsystems INSTANCE = new LauncherSubsystems();

        private LauncherSubsystems() {
            super(
                    Launcher.INSTANCE,
                    Gate.INSTANCE
            );
        }
        public Command start() {
            return new InstantCommand(() -> {
                ActiveOpMode.telemetry().addData("Hello", "Team");
                ActiveOpMode.telemetry().update();
            });
        }
    }

