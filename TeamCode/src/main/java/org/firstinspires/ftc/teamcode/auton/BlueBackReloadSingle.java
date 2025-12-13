package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackPickUpTwo;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackPickUpTwoStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackScorePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.blueBackStartPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.bluebackSafePose;
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

@Autonomous(name = "Blue - Back Zone - Reload Single", group = "2")
public class BlueBackReloadSingle extends VikingForceAutonBase {
    private TelemetryManager panelsTelemetry;

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain leave;

    public BlueBackReloadSingle() {
        super(blueBackStartPose, ShootingPosition.BACK);
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
                new ParallelGroup(
                        Gate.INSTANCE.close,
                        new FollowPath(moveToPickUpOne, false, 0.75),
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
                Gate.INSTANCE.close,
                new ParallelGroup(
                        Launcher.INSTANCE.stop,
                        IntakeSubsystem.INSTANCE.stop,
                        new FollowPath(leave, true, 0.75)
                )

        );
    }

    void buildPaths() {
        scorePreload = new Path(new BezierLine(blueBackStartPose, blueBackScorePose));
        scorePreload.setLinearHeadingInterpolation(blueBackStartPose.getHeading(), blueBackScorePose.getHeading());

        moveToPickUpOne = PedroComponent.follower().pathBuilder().addPath(new BezierLine(blueBackScorePose, blueBackPickUpOneStage)).setLinearHeadingInterpolation(blueBackScorePose.getHeading(), blueBackPickUpOneStage.getHeading()).build();

        doPickUpOne = PedroComponent.follower().pathBuilder().addPath(new BezierLine(blueBackPickUpOneStage, blueBackPickUpOne)).setLinearHeadingInterpolation(blueBackPickUpOneStage.getHeading(), blueBackPickUpOne.getHeading()).build();

        scorePickUpOne = PedroComponent.follower().pathBuilder().addPath(new BezierLine(blueBackPickUpOne, blueBackScorePose)).setLinearHeadingInterpolation(blueBackPickUpOne.getHeading(), blueBackScorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder().addPath(new BezierLine(blueBackScorePose, bluebackSafePose)).setLinearHeadingInterpolation(blueBackScorePose.getHeading(), bluebackSafePose.getHeading()).build();
    }

}
