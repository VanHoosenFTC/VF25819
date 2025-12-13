package org.firstinspires.ftc.teamcode.teleop;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

@TeleOp(name = "Pedro - Robot Centric TeleOp")
public class PedroRobotCentricTeleOp extends AbstractDriveTeleOp {
    private boolean driverControlled = true;

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
                Gamepads.gamepad1().rightStickX().negate()
        );


    }

    @Override
    public void onInit() {
        super.onInit();
        driverControlled = true;
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        follower().startTeleOpDrive();
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(new InstantCommand(() -> {
            driverControlled = false;
            follower().turn(0, false);

        }));

        Gamepads.gamepad1().rightBumper().whenBecomesFalse(new InstantCommand(() -> {
            driverControlled = true;
            follower().startTeleOpDrive();
        }));
    }
}
