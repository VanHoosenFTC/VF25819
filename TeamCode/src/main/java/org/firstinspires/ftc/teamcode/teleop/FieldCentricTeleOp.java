package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;

@TeleOp(name = "Field Centric TeleOp")
public class FieldCentricTeleOp extends AbstractDriveTeleOp {
    private final IMUEx imu = new IMUEx("imu", Direction.RIGHT, Direction.UP).zeroed();

    @Override
    Command getDriverControlledCommand() {
        return new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX(),
                new FieldCentric(imu)
        );
    }
}