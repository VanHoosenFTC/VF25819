package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontPickUpTwo;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontPickUpTwoStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontSafePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontScorePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontStartPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.shootingTime;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ShootingPosition;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous(name = "Red - Front Zone - Reload Single", group = "2")
public class RedFrontReloadSingle extends VikingForceAutonBase {

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain leave;

    public RedFrontReloadSingle() {
        super(redFrontStartPose, ShootingPosition.TOP);
    }

    Command autonomousRoutine() {
        return new SequentialGroup(
                new ParallelGroup(
                        Launcher.INSTANCE.start,
                        new FollowPath(scorePreload, true, 0.75),
                        IntakeSubsystem.INSTANCE.start
                ),
                new WaitUntil(Launcher.INSTANCE::nearGoal),
                Gate.INSTANCE.open,
                new Delay(shootingTime),
                Gate.INSTANCE.close,
                new WaitUntil(Launcher.INSTANCE::nearGoal),
                Gate.INSTANCE.open,
                new Delay(shootingTime),
                Gate.INSTANCE.close,
                new WaitUntil(Launcher.INSTANCE::nearGoal),
                Gate.INSTANCE.open,
                new Delay(shootingTime),
                Gate.INSTANCE.close,
                new FollowPath(moveToPickUpOne, false, 0.75),
                new FollowPath(doPickUpOne, false, 0.75),
                new FollowPath(scorePickUpOne, true, 0.75),
                new WaitUntil(Launcher.INSTANCE::nearGoal),
                Gate.INSTANCE.open,
                new Delay(shootingTime),
                Gate.INSTANCE.close,
                new WaitUntil(Launcher.INSTANCE::nearGoal),
                Gate.INSTANCE.open,
                new Delay(shootingTime),
                Gate.INSTANCE.close,
                new WaitUntil(Launcher.INSTANCE::nearGoal),
                Gate.INSTANCE.open,
                new Delay(shootingTime),
                Gate.INSTANCE.close,
                new ParallelGroup(
                        Launcher.INSTANCE.stop,
                        IntakeSubsystem.INSTANCE.stop,
                        new FollowPath(leave, true, 0.75)
                )

        );
    }

    void buildPaths() {
        scorePreload = new Path(new BezierLine(redFrontStartPose, redFrontScorePose));
        scorePreload.setLinearHeadingInterpolation(redFrontStartPose.getHeading(), redFrontScorePose.getHeading());

        moveToPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontScorePose, redFrontPickUpOneStage))
                .setLinearHeadingInterpolation(redFrontScorePose.getHeading(), redFrontPickUpOneStage.getHeading()).build();

        doPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontPickUpOneStage, redFrontPickUpOne))
                .setLinearHeadingInterpolation(redFrontPickUpOneStage.getHeading(), redFrontPickUpOne.getHeading()).build();

        scorePickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontPickUpOne, redFrontScorePose))
                .setLinearHeadingInterpolation(redFrontPickUpOne.getHeading(), redFrontScorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontScorePose, redFrontSafePose))
                .setLinearHeadingInterpolation(redFrontScorePose.getHeading(), redFrontSafePose.getHeading()).build();
        ;
    }
}
