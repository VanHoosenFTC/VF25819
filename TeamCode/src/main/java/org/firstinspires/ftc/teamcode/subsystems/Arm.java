package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;

/**
 * The lift subsystem implemented from
 * <a href="https://nextftc.dev/guide/subsystems/lift">docs</a>.
 */
public class Arm implements Subsystem {
    public static final Arm INSTANCE = new Arm();
    private Arm() { }

    private MotorEx motor = new MotorEx("ARM");

    private ControlSystem controlSystem = ControlSystem.builder()
            .posPid(0.1, 0, 0)
            .elevatorFF(0)
            .build();

    public Command toLow = new InstantCommand(() -> {
        ActiveOpMode.telemetry().addData("arm target: ", 0);
        new RunToPosition(controlSystem, 0).requires(this).schedule();

    });
    public Command toMiddle = new RunToPosition(controlSystem, 500).requires(this);
    public Command toHigh = new InstantCommand(() -> {
        ActiveOpMode.telemetry().addData("arm target: ", 1200);
        new RunToPosition(controlSystem, 0).requires(this).schedule();

    });

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("setting arm position", controlSystem.calculate(motor.getState()));
        motor.setPower(controlSystem.calculate(motor.getState()));
    }
}
