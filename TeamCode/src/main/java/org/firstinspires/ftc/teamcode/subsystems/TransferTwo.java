package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.powerable.SetPower;

public class TransferTwo implements Subsystem {
    public static final TransferTwo INSTANCE = new TransferTwo();
    private TransferTwo() { }

    private CRServoEx servo = new CRServoEx("TRANSFER2");

    public Command start = new SetPower(servo, 1).requires(this);
    public Command idle = new SetPower(servo, 0.1).requires(this);
    public Command stop = new SetPower(servo, 0).requires(this);
    public Command reverse = new SetPower(servo, -1).requires(this);
}
