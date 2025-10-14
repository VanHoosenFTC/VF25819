package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.ActiveOpMode;

public class IntakeSubsystem extends SubsystemGroup {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();

    private IntakeSubsystem() {
        super(
                Intake.INSTANCE,
                Gate.INSTANCE
        );
    }
    public Command start() {
        return new InstantCommand(() -> {
            ActiveOpMode.telemetry().addData("Hello", "Harshal");
            ActiveOpMode.telemetry().update();
        });
    }
}

