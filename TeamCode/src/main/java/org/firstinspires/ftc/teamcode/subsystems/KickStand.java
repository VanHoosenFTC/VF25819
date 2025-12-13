package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

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
    public static int PARKED_POSITION = -800;
    public static int TRAVEL_POSITION = 0;
    public static int KICKSTAND_VELOCITY = 500;
    public static int adjustment = 25;
    private int position = TRAVEL_POSITION;

    private KickStand() { }

    public ControlSystem controlSystem = ControlSystem.builder()
            .posPid(.005, 0, 0)
            .elevatorFF(0)
            .build();

    private DcMotorEx motor;

    public Command travel = new InstantCommand(() -> {
        position = TRAVEL_POSITION;
    }).requires(this);
    public Command park = new InstantCommand(() -> {
        position = PARKED_POSITION;
    }).requires(this);

    public Command raise = new InstantCommand(() -> {
        position = position + adjustment;
    }).requires(this);

    public Command lower = new InstantCommand(() -> {
        position = position - adjustment;
    }).requires(this);

    @Override
    public void periodic() {
        motor.setTargetPosition(position);
        motor.setVelocity(KICKSTAND_VELOCITY);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ActiveOpMode.telemetry().addData("Kickstand position", position);
    }

    @Override
    public void initialize() {
        motor = ActiveOpMode.hardwareMap().get(DcMotorEx.class, "KICKSTAND");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //motor.setCurrentPosition(0);
    }
}

