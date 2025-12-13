package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpTwo;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpTwoStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackScorePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackStartPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.shootingTime;

import com.bylazar.telemetry.TelemetryManager;
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

@Autonomous(name = "Red - Back Zone - Reload Double", group = "1")
public class RedBackReloadDouble extends VikingForceAutonBase {
    private TelemetryManager panelsTelemetry;

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain moveToPickUpTwo;

    private PathChain doPickUpTwo;

    private PathChain scorePickUpTwo;
    private PathChain leave;

    public RedBackReloadDouble() {
        super(redBackStartPose, ShootingPosition.BACK);
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
                new Delay(shootingTime),
                new ParallelGroup(
                        Gate.INSTANCE.close,
                        new FollowPath(moveToPickUpOne, false, 1.00),
                        Launcher.INSTANCE.stop
                ),
                new FollowPath(doPickUpOne, false, 1.00),
                new ParallelGroup(
                        new FollowPath(scorePickUpOne, true, 1.00),
                        Launcher.INSTANCE.start
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
                new ParallelGroup(
                        Gate.INSTANCE.close,
                        new FollowPath(moveToPickUpTwo, false, 1.00),
                        Launcher.INSTANCE.stop
                ),
                new FollowPath(doPickUpTwo, false, 1.00),
                new ParallelGroup(
                        new FollowPath(scorePickUpTwo, true, 1.00),
                        Launcher.INSTANCE.start
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
                new ParallelGroup(
                        Launcher.INSTANCE.stop,
                        IntakeSubsystem.INSTANCE.stop,
                        new FollowPath(leave, true, 1.00)
                )

        );
    }

    void buildPaths() {
        scorePreload = new Path(new BezierLine(redBackStartPose, redBackScorePose));
        scorePreload.setLinearHeadingInterpolation(redBackStartPose.getHeading(), redBackScorePose.getHeading());

        moveToPickUpOne = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackScorePose, redBackPickUpOneStage)).setLinearHeadingInterpolation(redBackScorePose.getHeading(), redBackPickUpOneStage.getHeading()).build();

        doPickUpOne = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackPickUpOneStage, redBackPickUpOne)).setLinearHeadingInterpolation(redBackPickUpOneStage.getHeading(), redBackPickUpOne.getHeading()).build();

        scorePickUpOne = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackPickUpOne, redBackScorePose)).setLinearHeadingInterpolation(redBackPickUpOne.getHeading(), redBackScorePose.getHeading()).build();

        moveToPickUpTwo = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackScorePose, redBackPickUpTwoStage)).setLinearHeadingInterpolation(redBackScorePose.getHeading(), redBackPickUpTwoStage.getHeading()).build();

        doPickUpTwo = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackPickUpTwoStage, redBackPickUpTwo)).setLinearHeadingInterpolation(redBackPickUpTwoStage.getHeading(), redBackPickUpTwo.getHeading()).build();

        scorePickUpTwo = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackPickUpTwo, redBackScorePose)).setLinearHeadingInterpolation(redBackPickUpTwo.getHeading(), redBackScorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackScorePose, redBackEndPose)).setLinearHeadingInterpolation(redBackScorePose.getHeading(), redBackEndPose.getHeading()).build();
    }

}