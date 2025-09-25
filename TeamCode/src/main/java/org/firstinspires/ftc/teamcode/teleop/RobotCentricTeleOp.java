package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Robot Centric TeleOp")
public class RobotCentricTeleOp extends NextFTCOpMode {
    public RobotCentricTeleOp() {
        addComponents(
                new SubsystemComponent(Intake.INSTANCE),
                new SubsystemComponent(Launcher.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    // change the names and directions to suit your robot
    private final MotorEx frontLeftMotor = new MotorEx(LEFT_FRONT_MOTOR_NAME);
    private final MotorEx frontRightMotor = new MotorEx(RIGHT_FRONT_MOTOR_NAME).reversed();
    private final MotorEx backLeftMotor = new MotorEx(LEFT_REAR_MOTOR_NAME);
    private final MotorEx backRightMotor = new MotorEx(RIGHT_REAR_MOTOR_NAME).reversed();

    @Override
    public void onStartButtonPressed() {
        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
        driverControlled.schedule();

        Gamepads.gamepad2().x().whenBecomesTrue(Intake.INSTANCE.start);
        Gamepads.gamepad2().a().whenBecomesTrue(Intake.INSTANCE.stop);
        Gamepads.gamepad2().b().whenBecomesTrue(Intake.INSTANCE.reverse);

        Gamepads.gamepad2().rightTrigger ().atLeast(50).whenBecomesTrue(Launcher.INSTANCE.start);
        Gamepads.gamepad2().rightTrigger().lessThan(50).whenBecomesTrue(Launcher.INSTANCE.stop);

    }
}