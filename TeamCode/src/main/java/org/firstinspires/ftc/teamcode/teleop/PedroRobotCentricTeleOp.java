package org.firstinspires.ftc.teamcode.teleop;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

@TeleOp(name = "Pedro - Robot Centric TeleOp")
public class PedroRobotCentricTeleOp extends AbstractDriveTeleOp {

    public PedroRobotCentricTeleOp() {
        super();
        addComponents(
                new PedroComponent(Constants::createFollower)
        );
    }
    @NonNull
    Command getDriverControlledCommand() {
        return new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
    }
}