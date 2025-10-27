package org.firstinspires.ftc.teamcode.auton;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
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

@Autonomous(name = "Blue - Back Zone")
public class BlueBack extends NextFTCOpMode {
    private final Pose startPose = new Pose(60, 7, Math.toRadians(270));
    private final Pose scorePose = new Pose(60, 15, Math.toRadians(295));
    private final Pose endPose = new Pose(60, 48, Math.toRadians(360));

    private Path scorePreload;
    private PathChain leave;

    public BlueBack() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE),
                BulkReadComponent.INSTANCE
        );
    }

    private Command autonomousRoutine() {
        return new SequentialGroup(
                Lift.INSTANCE.load,
                new FollowPath(scorePreload, true, 0.5),
                LauncherSubsystem.INSTANCE.launch,
                new ParallelGroup(
                    IntakeSubsystem.INSTANCE.start,
                    new FollowPath(leave, true, 0.75)
                ),
                new Delay(0.5),
                IntakeSubsystem.INSTANCE.stop
        );
    }

    @Override
    public void onStartButtonPressed() {
        buildPaths();
        PedroComponent.follower().setStartingPose(startPose);
        Launcher.setPowerFactor(.8);
        autonomousRoutine().schedule();
    }

    private void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(scorePose, endPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), endPose.getHeading()).build();;
    }
}
