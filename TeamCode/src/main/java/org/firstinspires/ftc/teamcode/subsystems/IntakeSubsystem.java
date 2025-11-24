package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.SubsystemGroup;

public class IntakeSubsystem extends SubsystemGroup {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();

    private IntakeSubsystem() {
        super(
                Intake.INSTANCE,
                TransferOne.INSTANCE,
                TransferTwo.INSTANCE
        );
    }

    public Command start =
            new ParallelGroup(Intake.INSTANCE.start,
                    TransferOne.INSTANCE.start,
                    TransferTwo.INSTANCE.start);

    public Command idle =
            new ParallelGroup(Intake.INSTANCE.idle,
                    TransferOne.INSTANCE.idle,
                    TransferTwo.INSTANCE.idle);

    public Command stop =
            new ParallelGroup(Intake.INSTANCE.stop,
                    TransferOne.INSTANCE.stop,
                    TransferTwo.INSTANCE.stop);


    public Command reverse =
            new ParallelGroup(Intake.INSTANCE.reverse,
                    TransferOne.INSTANCE.reverse,
                    TransferTwo.INSTANCE.reverse);

}
