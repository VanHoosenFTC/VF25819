package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

@Configurable
public class Gate implements Subsystem {
    public static final Gate INSTANCE = new Gate();

    public static double gateOneClosedPosition = 0.35;
    public static double gateOneOpenedPosition = 0.1;
    public static double gateOneReversedPosition = 0.65;

    public static double gateTwoClosedPosition = 0.6;
    public static double gateTwoOpenedPosition = 0.9;
    public static double gateTwoReversedPosition = 0.35;

    private double gateOnePosition;
    private double gateTwoPosition;

    private Gate() { }

    private final ServoEx gate1 = new ServoEx("GATE1");
    private final ServoEx gate2 = new ServoEx("GATE2");

    public Command open = new InstantCommand(() -> {
        gateOnePosition = gateOneOpenedPosition;
        gateTwoPosition = gateTwoOpenedPosition;
    }).requires(this);

    public Command close = new InstantCommand(() -> {
        gateOnePosition = gateOneClosedPosition;
        gateTwoPosition = gateTwoClosedPosition;
    }).requires(this);

    public Command reverse = new InstantCommand(() -> {
        gateOnePosition = gateOneReversedPosition;
        gateTwoPosition = gateTwoReversedPosition;
    }).requires(this);

    @Override
    public void periodic() {
        gate1.setPosition(gateOnePosition);
        gate2.setPosition(gateTwoPosition);
    }


}
