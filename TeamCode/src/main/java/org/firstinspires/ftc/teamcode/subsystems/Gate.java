package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Gate implements Subsystem {
    public static final Gate INSTANCE = new Gate();
    private Gate() { }

    private final ServoEx servo = new ServoEx("GATE");

    public Command open = new SetPosition(servo, 1).requires(this);

    public Command close = new SetPosition(servo, 0.5).requires(this);

    @Override
    public void periodic() {
        // nothing to do here
    }


}
