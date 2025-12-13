package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redFrontPickUpOneStage;
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

@Autonomous(name = "Red - Front Zone - No Reload", group = "3")
public class RedFrontNoReload extends VikingForceAutonBase {

    private Path scorePreload;

    private PathChain leave;

    public RedFrontNoReload() {
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

        leave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(redFrontScorePose, redFrontSafePose))
                .setLinearHeadingInterpolation(redFrontScorePose.getHeading(), redFrontSafePose.getHeading()).build();
        ;
    }
}
