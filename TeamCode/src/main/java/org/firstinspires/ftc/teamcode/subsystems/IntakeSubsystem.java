package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.SubsystemGroup;

public class IntakeSubsystem extends SubsystemGroup {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();

    private IntakeSubsystem() {
        super(
                Intake.INSTANCE,
                TransferOne.INSTANCE
        );
    }

    public Command start =
            new SequentialGroup(Intake.INSTANCE.start);

    public Command stop =
            new SequentialGroup(Intake.INSTANCE.stop);


    public Command reverse =
            new SequentialGroup(Intake.INSTANCE.reverse);

}
