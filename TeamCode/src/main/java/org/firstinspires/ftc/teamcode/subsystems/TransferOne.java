package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class TransferOne implements Subsystem {
    public static final TransferOne INSTANCE = new TransferOne();
    private TransferOne() { }

    private CRServoEx servo = new CRServoEx("TRANSFER1");

    public Command start = new SetPower(servo, -1).requires(this);
    public Command idle = new SetPower(servo, -0.1).requires(this);
    public Command stop = new SetPower(servo, 0).requires(this);
    public Command reverse = new SetPower(servo, 1).requires(this);
}
