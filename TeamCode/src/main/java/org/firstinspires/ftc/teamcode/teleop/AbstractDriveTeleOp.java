package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import org.firstinspires.ftc.teamcode.subsystems.DynamicLauncher;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Tilt;

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
                new SubsystemComponent(DynamicLauncher.INSTANCE,
                        Tilt.INSTANCE,
                        Lift.INSTANCE,
                        Gate.INSTANCE,
                        Intake.INSTANCE),
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

        Gamepads.gamepad2().leftStickY().atLeast(0.5).whenBecomesTrue(DynamicLauncher.INSTANCE.start);
        Gamepads.gamepad2().leftStickY().lessThan(0.5).whenBecomesTrue(DynamicLauncher.INSTANCE.stop);

        Gamepads.gamepad2().rightStickButton().whenBecomesTrue(Lift.INSTANCE.score).whenBecomesFalse(Lift.INSTANCE.load);

        Gamepads.gamepad2().rightBumper().whenBecomesTrue(DynamicLauncher.INSTANCE.adjustPowerFactor(0.05));
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(DynamicLauncher.INSTANCE.adjustPowerFactor(-0.05));

        Gamepads.gamepad2().dpadLeft().whenBecomesTrue(Gate.INSTANCE.open);
        Gamepads.gamepad2().dpadRight().whenBecomesTrue(Gate.INSTANCE.close);

        Gamepads.gamepad2().dpadUp().whenBecomesTrue(Tilt.INSTANCE.adjust(0.01));
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(Tilt.INSTANCE.adjust(-0.01));

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

    }

    abstract Command getDriverControlledCommand();
}
