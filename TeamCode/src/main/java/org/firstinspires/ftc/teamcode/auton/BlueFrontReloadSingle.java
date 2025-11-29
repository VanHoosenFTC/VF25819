package org.firstinspires.ftc.teamcode.auton;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Blue - Front Zone - Reload Single", group = "2")
public class BlueFrontReloadSingle extends NextFTCOpMode {
    private final Pose startPose = new Pose(30, 136, Math.toRadians(270));
    private final Pose scorePose = new Pose(51, 84, Math.toRadians(310));
    private final Pose endPose = new Pose(30, 48, Math.toRadians(180));

    private static final Pose pickUpOneStage = new Pose(42, 74, Math.toRadians(180));
    private static final Pose pickUpOne= new Pose(13, 74, Math.toRadians(180));

    private TelemetryManager panelsTelemetry;

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain leave;

    public BlueFrontReloadSingle() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(LauncherSubsystem.INSTANCE),
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
                        IntakeSubsystem.INSTANCE.start,
                        new FollowPath(moveToPickUpOne, false, 1.00)
                ),
                new FollowPath(doPickUpOne, true, 1.00),
                IntakeSubsystem.INSTANCE.idle,
                new ParallelGroup(
                        Launcher.INSTANCE.start,
                        new FollowPath(scorePickUpOne, true, 0.9)
                ),
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
        Launcher.setPowerFactor(AutonConstants.TopLauncherPercent);
        autonomousRoutine().schedule();
    }

    private void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        moveToPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(scorePose, pickUpOneStage))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickUpOneStage.getHeading()).build();

        doPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(pickUpOneStage, pickUpOne))
                .setLinearHeadingInterpolation(pickUpOneStage.getHeading(), pickUpOne.getHeading()).build();

        scorePickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(pickUpOne, scorePose))
                .setLinearHeadingInterpolation(pickUpOne.getHeading(), scorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(scorePose, endPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), endPose.getHeading()).build();;
    }


    @Override
    public void onInit() {
        super.onInit();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Gate.INSTANCE.open.schedule();
        Gate.INSTANCE.close.schedule();
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
