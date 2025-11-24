package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.BackLauncherPercent;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Lift;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Configurable
@Autonomous(name = "Blue - Back Zone - No Reload", group = "3")
public class BlueBackNoReload extends NextFTCOpMode {
    private static final Pose startPose = new Pose(56.5, 10, Math.toRadians(270));
    private static final Pose scorePose = new Pose(59, 17, Math.toRadians(292));

    private static final Pose endPose = new Pose(48, 50, Math.toRadians(0));


    private TelemetryManager panelsTelemetry;

    private Path scorePreload;

    private PathChain leave;

    public BlueBackNoReload() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE),
                BulkReadComponent.INSTANCE
        );
    }

    private Command autonomousRoutine() {
        return new SequentialGroup(
                Launcher.INSTANCE.start,
                new FollowPath(scorePreload, true, 0.9),
                IntakeSubsystem.INSTANCE.start,
                new Delay(0.2),
                Gate.INSTANCE.open,
                new Delay(4),
                IntakeSubsystem.INSTANCE.stop,
                Gate.INSTANCE.close,
                new ParallelGroup(
                        Launcher.INSTANCE.stop,
                        new FollowPath(leave, true, 1.00)
                )

        );
    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setStartingPose(startPose);
        PedroComponent.follower().setPose(startPose);
        buildPaths();
        Launcher.setPowerFactor(BackLauncherPercent);
        Lift.INSTANCE.load.schedule();
        autonomousRoutine().schedule();
    }

    private void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(scorePose, endPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), endPose.getHeading()).build();
    }

    public void onUpdate() {
        // Log to Panels and driver station (custom log function)
        log("X", PedroComponent.follower().getPose().getX());
        log("Y", PedroComponent.follower().getPose().getY());
        log("Heading", PedroComponent.follower().getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void onInit() {
        super.onInit();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Gate.INSTANCE.open.schedule();
        Gate.INSTANCE.close.schedule();
        IntakeSubsystem.INSTANCE.idle.schedule();
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
