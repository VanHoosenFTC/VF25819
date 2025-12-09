package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class KickStand implements Subsystem {
    public static final KickStand INSTANCE = new KickStand();
    public static int PARKED_POSITION = -1300;
    public static int TRAVEL_POSITION = 50;

    private KickStand() { }

    public ControlSystem controlSystem = ControlSystem.builder()
            .posPid(.005, 0, 0)
            .elevatorFF(0)
            .build();

    private MotorEx motor = new MotorEx("KICKSTAND");

    public Command travel = new InstantCommand(() -> {
        ActiveOpMode.telemetry().addData("Kickstand position", motor.getCurrentPosition());
        new RunToPosition(controlSystem, TRAVEL_POSITION).schedule();
    }).requires(this);
    public Command park = new InstantCommand(() -> {
        ActiveOpMode.telemetry().addData("Kickstand position", motor.getCurrentPosition());
        new RunToPosition(controlSystem, PARKED_POSITION).schedule();
    }).requires(this);

    @Override
    public void periodic() {
        motor.setPower(controlSystem.calculate(motor.getState()));
    }

    @Override
    public void initialize() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //motor.setCurrentPosition(0);
    }
}

