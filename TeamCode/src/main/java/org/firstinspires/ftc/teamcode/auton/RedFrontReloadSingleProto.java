package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontProtoEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontProtoPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontProtoPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontProtoScorePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueFrontProtoStartPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontProtoEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontProtoPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontProtoPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontProtoScorePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontProtoStartPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.shootingTime;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.thirdShootingTime;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
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

@Autonomous(name = "red - Front Zone - Reload Single Proto", group = "4")
public class RedFrontReloadSingleProto extends VikingForceAutonBase {

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain leave;

    public RedFrontReloadSingleProto() {
        super(redFrontProtoStartPose, ShootingPosition.TOP);
    }

    public RedFrontReloadSingleProto(Pose startPose, ShootingPosition shootingPosition) {
        super(startPose, shootingPosition);
    }

    Command autonomousRoutine() {
        return new SequentialGroup(
                new ParallelGroup(
                        Launcher.INSTANCE.start,
                        new FollowPath(scorePreload, true, 1.00),
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
                new Delay(thirdShootingTime),
                Gate.INSTANCE.close,
                new FollowPath(moveToPickUpOne, false, 1.00),
                new FollowPath(doPickUpOne, false, 1.00),
                new FollowPath(scorePickUpOne, true, 1.00),
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
                new Delay(thirdShootingTime),
                Gate.INSTANCE.close,
//                new TurnTo(Angle.fromDeg(0)),
                new ParallelGroup(
                        Launcher.INSTANCE.stop,
                        IntakeSubsystem.INSTANCE.stop,
                        new FollowPath(leave, true, 1.00)
                )

        );
    }

    void buildPaths() {
        scorePreload = new Path(new BezierLine(redFrontProtoStartPose, redFrontProtoScorePose));
        scorePreload.setLinearHeadingInterpolation(redFrontProtoStartPose.getHeading(), redFrontProtoScorePose.getHeading());

        moveToPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontProtoScorePose, redFrontProtoPickUpOneStage))
                .setLinearHeadingInterpolation(redFrontProtoScorePose.getHeading(), redFrontProtoPickUpOneStage.getHeading()).build();

        doPickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontProtoPickUpOneStage, redFrontProtoPickUpOne))
                .setLinearHeadingInterpolation(redFrontProtoPickUpOneStage.getHeading(), redFrontProtoPickUpOne.getHeading()).build();

        scorePickUpOne = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontProtoPickUpOne, redFrontProtoScorePose))
                .setLinearHeadingInterpolation(redFrontProtoPickUpOne.getHeading(), redFrontProtoScorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontProtoScorePose, redFrontProtoEndPose))
                .setLinearHeadingInterpolation(redFrontProtoScorePose.getHeading(), redFrontProtoEndPose.getHeading()).build();
        ;
    }
}
