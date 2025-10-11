package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.MotorEx;

public abstract class AbstractDriveTeleOp extends NextFTCOpMode {
    // change the names and directions to suit your robot
    protected final MotorEx frontLeftMotor = new MotorEx(LEFT_FRONT_MOTOR_NAME).reversed();
    protected final MotorEx frontRightMotor = new MotorEx(RIGHT_FRONT_MOTOR_NAME);
    protected final MotorEx backLeftMotor = new MotorEx(LEFT_REAR_MOTOR_NAME).reversed();
    protected final MotorEx backRightMotor = new MotorEx(RIGHT_REAR_MOTOR_NAME);

    public AbstractDriveTeleOp() {
        addComponents(
                new SubsystemComponent(Intake.INSTANCE),
                new SubsystemComponent(Launcher.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        Command driverControlled = getDriverControlledCommand();
        driverControlled.schedule();

        Gamepads.gamepad2().x().whenBecomesTrue(Intake.INSTANCE.start);
        Gamepads.gamepad2().a().whenBecomesTrue(Intake.INSTANCE.stop);
        Gamepads.gamepad2().b().whenBecomesTrue(Intake.INSTANCE.reverse);

        Gamepads.gamepad2().rightTrigger().atLeast(0.5).whenBecomesTrue(Launcher.INSTANCE.start);
        Gamepads.gamepad2().rightTrigger().lessThan(0.5).whenBecomesTrue(Launcher.INSTANCE.stop);

        Gamepads.gamepad2().rightBumper().whenBecomesTrue(Launcher.INSTANCE.adjustPowerFactor(0.05));
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(Launcher.INSTANCE.adjustPowerFactor(-0.05));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        telemetry.addData("righttrigger value", Gamepads.gamepad2().rightTrigger().get().doubleValue());
        telemetry.update();

    }

    abstract Command getDriverControlledCommand();
}
