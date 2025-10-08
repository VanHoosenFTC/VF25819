package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import com.pedropathing.telemetry.SelectScope;
import com.pedropathing.telemetry.SelectableOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ChassisConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveMotor;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Drive Motor Direction Test")
public class DriveMotorDirectionTestTeleOp extends NextFTCOpMode {

    public DriveMotor frontLeftMotor;
    public DriveMotor frontRightMotor;
    public DriveMotor backLeftMotor;
    public DriveMotor backRightMotor;


    public DriveMotorDirectionTestTeleOp () {
        frontLeftMotor = new DriveMotor(LEFT_FRONT_MOTOR_NAME);
        frontRightMotor = new DriveMotor(RIGHT_FRONT_MOTOR_NAME);
        backLeftMotor = new DriveMotor(LEFT_REAR_MOTOR_NAME);
        backRightMotor = new DriveMotor(RIGHT_REAR_MOTOR_NAME);
        addComponents(
                new SubsystemComponent(frontLeftMotor),
                new SubsystemComponent(frontRightMotor),
                new SubsystemComponent(backLeftMotor),
                new SubsystemComponent(backRightMotor),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {

        Gamepads.gamepad2().a().whenBecomesTrue(frontLeftMotor.start);
        Gamepads.gamepad2().b().whenBecomesTrue(frontRightMotor.start);
        Gamepads.gamepad2().x().whenBecomesTrue(backLeftMotor.start);
        Gamepads.gamepad2().y().whenBecomesTrue(backRightMotor.start);

    }

}
