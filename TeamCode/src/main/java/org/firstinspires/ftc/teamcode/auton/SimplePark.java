package org.firstinspires.ftc.teamcode.auton;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class SimplePark extends OpMode {

    private boolean isRedAlliance = true;
    private Follower follower;

    private PoseFactory poseFactory = PoseFactory.degrees();

    public Pose start = poseFactory.of(10, 132, 90);
    public Pose park = poseFactory.of(10, 120, -90);

    public Path park() {
        return line(start, park).tangent();
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, park())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
    }
    @Override
    public void init_loop() {
        if (gamepad1.x) isRedAlliance = false;
        if (gamepad1.b) isRedAlliance = true;

        if (!isRedAlliance) {
            poseFactory = PoseFactory.degrees();
            poseFactory.mirrorX(70.75);
        } else {
            poseFactory = PoseFactory.degrees();
        }

        start = poseFactory.of(10, 132, 90);
        park = poseFactory.of(10, 120, -90);

        telemetry.addData("Selected Alliance is", isRedAlliance ? "red": "blue");
        telemetry.addData("Start coordinates", start.x() + ", " + start.y() + ", " + Math.toDegrees(start.heading()));

    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    // from the example code at the end of the autonomous page
    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());

        // We will add these two, but you can use the many other follower methods aswell in your own code!
        telemetry.addData("Path completion", follower.completion());
        telemetry.addData("Distance remaining in path", follower.remainingDistance());
        telemetry.update();
    }
}
