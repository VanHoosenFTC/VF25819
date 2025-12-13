package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontPickUpTwo;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontPickUpTwoStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontSafepose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontStartPose;
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

@Autonomous(name = "Blue - Front Zone - Reload Single", group = "2")
public class BlueFrontReloadSingle extends VikingForceAutonBase {

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain leave;

    public BlueFrontReloadSingle() {
        super(blueFrontStartPose, ShootingPosition.TOP);
    }
    Command autonomousRoutine() {
        return new SequentialGroup(
                new ParallelGroup(
                        Launcher.INSTANCE.start,
                        new FollowPath(scorePreload, true, 1.00),
                        IntakeSubsystem.INSTANCE.start
                ),
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
                new FollowPath(moveToPickUpOne, true, 0.75),
                new FollowPath(doPickUpOne, true, 0.75),
                new FollowPath(scorePickUpOne, true, 0.75),
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
        scorePreload = new Path(new BezierLine(blueFrontStartPose, AutonConstants.blueFrontScorePose));
        scorePreload.setLinearHeadingInterpolation(blueFrontStartPose.getHeading(), AutonConstants.blueFrontScorePose.getHeading());

        moveToPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(AutonConstants.blueFrontScorePose, blueFrontPickUpOneStage))
                .setLinearHeadingInterpolation(AutonConstants.blueFrontScorePose.getHeading(), blueFrontPickUpOneStage.getHeading()).build();

        doPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(blueFrontPickUpOneStage, blueFrontPickUpOne))
                .setLinearHeadingInterpolation(blueFrontPickUpOneStage.getHeading(), blueFrontPickUpOne.getHeading()).build();

        scorePickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(blueFrontPickUpOne, AutonConstants.blueFrontScorePose))
                .setLinearHeadingInterpolation(blueFrontPickUpOne.getHeading(), AutonConstants.blueFrontScorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(AutonConstants.blueFrontScorePose, blueFrontSafepose))
                .setLinearHeadingInterpolation(AutonConstants.blueFrontScorePose.getHeading(), blueFrontSafepose.getHeading()).build();;
    }

}
