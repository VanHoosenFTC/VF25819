package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackEndPose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpOne;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpOneStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpTwo;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackPickUpTwoStage;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackSafePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackScorePose;
import static org.firstinspires.ftc.teamcode.auton.AutonConstants.redBackStartPose;

import com.bylazar.configurables.annotations.Configurable;
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

@Configurable
@Autonomous(name = "Red - Back Zone - No Reload", group = "3")
public class RedBackNoReload extends NextFTCOpMode {

    private TelemetryManager panelsTelemetry;

    private Path scorePreload;
    private PathChain leave;

    public RedBackNoReload() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE),
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
                        new FollowPath(leave, true, 1.00)
                )

        );
    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setStartingPose(redBackStartPose);
        PedroComponent.follower().setPose(redBackStartPose);
        buildPaths();
        Launcher.setPowerFactor(AutonConstants.BackLauncherPercent);
        Lift.INSTANCE.load.schedule();
        autonomousRoutine().schedule();
    }

    private void buildPaths() {
        scorePreload = new Path(new BezierLine(redBackStartPose, redBackScorePose));
        scorePreload.setLinearHeadingInterpolation(redBackStartPose.getHeading(), redBackScorePose.getHeading());

        leave = PedroComponent.follower().pathBuilder().addPath(new BezierLine(redBackScorePose, redBackSafePose)).setLinearHeadingInterpolation(redBackScorePose.getHeading(), redBackSafePose.getHeading()).build();
    }


    public void onUpdate() {
        // Log to Panels and driver station (custom log function)
        log("X", PedroComponent.follower().getPose().getX());
        log("Y", PedroComponent.follower().getPose().getY());
        log("Heading", PedroComponent.follower().getPose().getHeading());
        telemetry.update();
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
