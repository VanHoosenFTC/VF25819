package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

@Configurable
public class Lift implements Subsystem {
    public static final Lift INSTANCE = new Lift();
    public static int DOWN_POSITION = 10;
    public static int up_POSITION = 0;
    public static int KICKSTAND_VELOCITY = 500;
    private int position = up_POSITION;

    private Lift() { }

    public ControlSystem controlSystem = ControlSystem.builder()
            .posPid(.005, 0, 0)
            .elevatorFF(0)
            .build();

    private DcMotorEx motor;

    public Command up = new InstantCommand(() -> {
        position = up_POSITION;
    }).requires(this);
    public Command down = new InstantCommand(() -> {
        position = DOWN_POSITION;
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
        motor = ActiveOpMode.hardwareMap().get(DcMotorEx.class, "Lift");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //motor.setCurrentPosition(0);
    }
}
