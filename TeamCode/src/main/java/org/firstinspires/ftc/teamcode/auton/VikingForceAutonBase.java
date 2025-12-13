package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontStartPose;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.ShootingPosition;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

public abstract class VikingForceAutonBase extends NextFTCOpMode {
    private TelemetryManager panelsTelemetry;
    private Pose startPose;
    private ShootingPosition shootingPosition;

    public VikingForceAutonBase(Pose startPose, ShootingPosition shootingPosition) {
        addComponents(new PedroComponent(Constants::createFollower), new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE), BulkReadComponent.INSTANCE);
        this.startPose = startPose;
        this.shootingPosition = shootingPosition;
    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setStartingPose(startPose);
        PedroComponent.follower().setPose(startPose);
        buildPaths();
        if (shootingPosition == ShootingPosition.TOP) {
            Launcher.setPowerFactor(AutonConstants.TopLauncherPercent);
        } else {
            Launcher.setPowerFactor(AutonConstants.BackLauncherPercent);
        }
        autonomousRoutine().schedule();
    }

    abstract void buildPaths();

    abstract Command autonomousRoutine();

    @Override
    public void onInit() {
        super.onInit();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Gate.INSTANCE.open.schedule();
        Gate.INSTANCE.close.schedule();
        IntakeSubsystem.INSTANCE.start.schedule();
        IntakeSubsystem.INSTANCE.stop.schedule();
        Intake.INSTANCE.idle.schedule();
    }

    private void log(String caption, Object... text) {
        if (text.length == 1) {
            telemetry.addData(caption, text[0]);
            panelsTelemetry.debug(caption + ": " + text[0]);
        } else if (text.length >= 2) {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < text.length; i++) {
                message.append(text[i]);
                if (i < text.length - 1) message.append(" ");
            }
            telemetry.addData(caption, message.toString());
            panelsTelemetry.debug(caption + ": " + message);
        }
    }
}
