package org.firstinspires.ftc.teamcode.auton;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import androidx.annotation.NonNull;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.JoeRobot;
import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous
public class SecondShooter extends NextOpMode {

    private JoeRobot joeRobot;

    private Follower follower;

    private Alliance selectedAlliance = Alliance.RED;

    private boolean hasInitializedHardware = false;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(60, 133, 90);
    private final Pose score = poseFactory.of(60, 125, 90);
    private final Pose park = poseFactory.of(14, 120, 90);

    public Path score() {
        return line(start, score).constant(score);
    }

    public Path park() {
        return line(score, park).constant(park);
    }

    public SecondShooter(@NonNull JoeRobot joeRobot) {
        super(joeRobot);
        this.joeRobot = joeRobot;
        Telemetry.addBackend(PanelsTelemetry.INSTANCE.getFtcTelemetry());
        Scheduler.reset();
    }

    private Command autoRoutine() {
        return sequential(
                // 1. Follow path to score location
                follow(follower, score()),

                // 2. Wait up to 10 seconds for vision condition
                race(
                        waitUntil(() -> joeRobot.getVision().isAllianceHiveTiltedUp(selectedAlliance)),
                        waitMs(18000)
                ),

                // 3. If hive is tilted up -> Launch then park
                //    If timeout hit (false) -> Skip launch, go straight to park
                conditional(
                        () -> joeRobot.getVision().isAllianceHiveTiltedUp(selectedAlliance),
                        sequential(
                                joeRobot.launch(),
                                waitMs(5000),
                                joeRobot.stopLauncher()
                        ),
                        instant(() -> {}) // Empty fallback command if condition was not met
                ),

                // 4. Always follow path to park
                follow(follower, park())
        );
    }

    @Override
    public void disabledPeriodic() {
        // Run hardware init sequence once during init
        if (!hasInitializedHardware) {
            follower = Constants.create(hardwareMap);
            follower.setPose(start);
            follower.update();
            joeRobot.init().schedule();
            hasInitializedHardware = true;
        }

        // Press B for Red or X for Blue on Gamepad 1 before pressing Play
        if (gamepad1.x) {
            selectedAlliance = Alliance.BLUE;
        } else if (gamepad1.b) {
            selectedAlliance = Alliance.RED;
        }

        joeRobot.setAlliance(selectedAlliance);

        // Prominent Telemetry check on Driver Station screen during Init
        Telemetry.log("ALLIANCE SETTING", selectedAlliance == Alliance.BLUE ? "🔵 BLUE ALLIANCE" : "🔴 RED ALLIANCE");
        Telemetry.log("Press GP1 X for BLUE | Press GP1 B for RED", "");
        Telemetry.log("X", follower.pose().x());
        Telemetry.log("Y", follower.pose().y());
        Telemetry.log("Heading", Math.toDegrees(follower.pose().heading()));
        Telemetry.log("Follower Mode", follower.mode());
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    // from the example code at the end of the autonomous page
    @Override
    public void periodic() {
        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here
        Telemetry.log("X", follower.pose().x());
        Telemetry.log("Y", follower.pose().y());
        Telemetry.log("Heading", Math.toDegrees(follower.pose().heading()));
        Telemetry.log("Follower Mode", follower.mode());

        // We will add these two, but you can use the many other follower methods aswell in your own code!
        Telemetry.log("Path completion", follower.completion());
        Telemetry.log("Distance remaining in path", follower.remainingDistance());
        Telemetry.log("hive is tilted up?", joeRobot.getVision().isAllianceHiveTiltedUp(selectedAlliance));
    }

}
