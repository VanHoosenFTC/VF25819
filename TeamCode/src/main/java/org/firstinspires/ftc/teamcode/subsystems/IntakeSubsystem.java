package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
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

    public Command start =
            new SequentialGroup(Intake.INSTANCE.start);

    public Command stop =
            new SequentialGroup(Intake.INSTANCE.stop
                    .thenWait(0.5)
                    .then(Gate.INSTANCE.close));

    public Command reverse =
            new SequentialGroup(Gate.INSTANCE.open
                    .thenWait(0.5)
                    .then(Intake.INSTANCE.reverse));
}

