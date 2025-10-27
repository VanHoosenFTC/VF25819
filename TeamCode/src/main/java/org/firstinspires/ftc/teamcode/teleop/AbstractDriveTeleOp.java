package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import org.firstinspires.ftc.teamcode.subsystems.Camera;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Light;
import org.firstinspires.ftc.teamcode.subsystems.Tilt;

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

    public AbstractDriveTeleOp() {
        addComponents(
                new SubsystemComponent(LauncherSubsystem.INSTANCE,
                        Tilt.INSTANCE,
                        IntakeSubsystem.INSTANCE,
                        Camera.INSTANCE,
                        Light.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        Command driverControlled = getDriverControlledCommand();
        driverControlled.schedule();

        Gamepads.gamepad1().rightTrigger().atLeast(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.start);
        Gamepads.gamepad1().rightTrigger().lessThan(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.stop);
        Gamepads.gamepad1().leftTrigger().atLeast(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.reverse);
        Gamepads.gamepad1().leftTrigger().lessThan(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.stop);

        Gamepads.gamepad1().start().whenBecomesTrue(new InstantCommand(imu::zero));

        Gamepads.gamepad2().start().whenBecomesTrue(Launcher.INSTANCE.reverse());

        Gamepads.gamepad2().leftStickY().atLeast(0.5).whenBecomesTrue(Launcher.INSTANCE.start);
        Gamepads.gamepad2().leftStickY().lessThan(0.5).whenBecomesTrue(Launcher.INSTANCE.stop);

        Gamepads.gamepad2().rightStickButton().whenBecomesTrue(Lift.INSTANCE.score).whenBecomesFalse(Lift.INSTANCE.load);

        Gamepads.gamepad2().rightBumper().whenBecomesTrue(Launcher.INSTANCE.adjustPowerFactor(0.01));
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(Launcher.INSTANCE.adjustPowerFactor(-0.01));

        Gamepads.gamepad2().dpadLeft().whenBecomesTrue(Gate.INSTANCE.open);
        Gamepads.gamepad2().dpadRight().whenBecomesTrue(Gate.INSTANCE.close);

        Gamepads.gamepad2().dpadUp().whenBecomesTrue(Tilt.INSTANCE.adjust(0.01));
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(Tilt.INSTANCE.adjust(-0.01));

        Gamepads.gamepad2().y().whenBecomesTrue(LauncherSubsystem.INSTANCE.launch);

        Gamepads.gamepad2().a().whenBecomesTrue(Light.INSTANCE.power(1));
        Gamepads.gamepad2().x().whenBecomesTrue(Light.INSTANCE.power(0));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ActiveOpMode.telemetry().update();

    }

    @Override
    public void onInit() {
        super.onInit();
        ActiveOpMode.telemetry().update();
        Lift.INSTANCE.load.schedule();

    }


    abstract Command getDriverControlledCommand();
}
