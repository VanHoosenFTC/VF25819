package org.firstinspires.ftc.teamcode.auton;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import androidx.annotation.NonNull;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.JoeRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous
public class MoveLeft extends NextOpMode {

    private JoeRobot joeRobot;

    private Follower follower;

    private PoseFactory poseFactory = PoseFactory.degrees();

    public Pose start = poseFactory.of(130, 20, 270);
    public Pose park = poseFactory.of(120, 20, 270);

    public MoveLeft(@NonNull JoeRobot joeRobot) {
        super(joeRobot);
        this.joeRobot = joeRobot;
        Telemetry.addBackend(PanelsTelemetry.INSTANCE.getFtcTelemetry());
        Scheduler.reset();
    }

    public Path park() {
        return line(start, park).linear(start, park);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, park())
        );
    }

    @Override
    public void disabledPeriodic() {
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
        Telemetry.log("X", follower.pose().x());
        Telemetry.log("Y", follower.pose().y());
        Telemetry.log("Heading", Math.toDegrees(follower.pose().heading()));
        Telemetry.log("Follower Mode", follower.mode());
        joeRobot.init().schedule();
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
    }
}
