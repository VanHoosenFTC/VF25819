package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.*;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ShootingPosition;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.core.units.Angle;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.TurnTo;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Red - Front Zone - Reload Double", group = "1")
public class RedFrontReloadDouble extends VikingForceAutonBase {

    private Path scorePreload;

    private PathChain moveToPickUpOne;

    private PathChain doPickUpOne;

    private PathChain scorePickUpOne;

    private PathChain moveToPickUpTwo;

    private PathChain doPickUpTwo;

    private PathChain scorePickUpTwo;
    private PathChain leave;

    public RedFrontReloadDouble() {
        super(redFrontStartPose, ShootingPosition.TOP);
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
                new Delay(shootingTime),
                Gate.INSTANCE.close,
//                new TurnTo(Angle.fromDeg(0)),
                new FollowPath(moveToPickUpTwo, true, 0.5


                ),
                new Delay(transitionWait),
                new FollowPath(doPickUpTwo, true, 1.00),
                new Delay(transitionWait),
                new FollowPath(scorePickUpTwo, true, 1.00),
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

        moveToPickUpTwo = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontScorePose, redFrontPickUpTwoStage))
                .setLinearHeadingInterpolation(redFrontScorePose.getHeading(), redFrontPickUpTwoStage.getHeading()).build();

        doPickUpTwo = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontPickUpTwoStage, redFrontPickUpTwo))
                .setLinearHeadingInterpolation(redFrontPickUpTwoStage.getHeading(), redFrontPickUpTwo.getHeading()).build();

        scorePickUpTwo = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontPickUpTwo, redFrontScorePose))
                .setLinearHeadingInterpolation(redFrontPickUpTwo.getHeading(), redFrontScorePose.getHeading()).build();

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontScorePose, redFrontEndPose))
                .setLinearHeadingInterpolation(redFrontScorePose.getHeading(), redFrontEndPose.getHeading()).build();
        ;
    }
}
