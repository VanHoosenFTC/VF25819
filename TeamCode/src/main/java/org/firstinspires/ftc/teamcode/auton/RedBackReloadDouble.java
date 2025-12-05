package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.*;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
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

@Autonomous(name = "Red - Back Zone - Reload Double", group = "1")
public class RedBackReloadDouble extends NextFTCOpMode {
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
        addComponents(new PedroComponent(Constants::createFollower), new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE), BulkReadComponent.INSTANCE);
    }

    private Command autonomousRoutine() {
        return new SequentialGroup(
                Launcher.INSTANCE.warmup,
                new FollowPath(scorePreload, true, 0.9),
                IntakeSubsystem.INSTANCE.start,
                Launcher.INSTANCE.start,
                new Delay(0.05),
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
                        Launcher.INSTANCE.warmup,
                        new FollowPath(scorePickUpOne, true, 0.9)
                ),
                IntakeSubsystem.INSTANCE.start,
                Launcher.INSTANCE.start,
                new Delay(0.05),
                Gate.INSTANCE.open,
                new Delay(4),
                IntakeSubsystem.INSTANCE.stop,
                Gate.INSTANCE.close,
                new ParallelGroup(
                        Launcher.INSTANCE.stop,
                        IntakeSubsystem.INSTANCE.start,
                        new FollowPath(moveToPickUpTwo, false, 1.00)
                ),
                new FollowPath(doPickUpTwo, true, 1.00),
                IntakeSubsystem.INSTANCE.idle,
                new ParallelGroup(
                        Launcher.INSTANCE.warmup,
                        new FollowPath(scorePickUpTwo, true, 0.9)
                ),
                IntakeSubsystem.INSTANCE.start,
                Launcher.INSTANCE.start,
                new Delay(0.05),
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
        PedroComponent.follower().setStartingPose(redBackStartPose);
        PedroComponent.follower().setPose(redBackStartPose);
        buildPaths();
        Launcher.setPowerFactor(BackLauncherPercent);
        autonomousRoutine().schedule();
    }

    private void buildPaths() {
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