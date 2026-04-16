package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import org.firstinspires.ftc.teamcode.ShootingPosition;
import org.firstinspires.ftc.teamcode.sensors.HuskyLensSensor;
import org.firstinspires.ftc.teamcode.sensors.KickstandColorSensor;
import org.firstinspires.ftc.teamcode.sensors.ArtifactColorSensor;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;

public abstract class AbstractDriveTeleOp extends NextFTCOpMode {
    protected final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.LEFT).zeroed();
    // change the names and directions to suit your robot
    protected final MotorEx frontLeftMotor = new MotorEx(LEFT_FRONT_MOTOR_NAME).reversed();
    protected final MotorEx frontRightMotor = new MotorEx(RIGHT_FRONT_MOTOR_NAME);
    protected final MotorEx backLeftMotor = new MotorEx(LEFT_REAR_MOTOR_NAME).reversed();
    protected final MotorEx backRightMotor = new MotorEx(RIGHT_REAR_MOTOR_NAME);

    protected KickstandColorSensor kickstandColorSensor = new KickstandColorSensor();
    protected ArtifactColorSensor artifactColorSensor = new ArtifactColorSensor();
    protected HuskyLensSensor huskyLensSensor = new HuskyLensSensor();

    private boolean autoLaunchingEnabled = true;

    public AbstractDriveTeleOp() {
        addComponents(
                new SubsystemComponent(Intake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        // Gamepads.gamepad2().leftTrigger().lessThan(0.5).whenBecomesTrue(Gate.INSTANCE.close);
        Gamepads.gamepad1().a().whenBecomesTrue(Intake.INSTANCE.start);
        Gamepads.gamepad1().b().whenBecomesTrue(Intake.INSTANCE.stop);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        ActiveOpMode.telemetry().addData("Auto Launcher Enabled", autoLaunchingEnabled);


        ActiveOpMode.telemetry().update();
    }

    @Override
    public void onInit() {
        super.onInit();
        ActiveOpMode.telemetry().update();

    }


    abstract Command getDriverControlledCommand();
}
