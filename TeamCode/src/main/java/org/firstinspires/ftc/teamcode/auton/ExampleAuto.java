package org.firstinspires.ftc.teamcode.auton;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class ExampleAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    // Poses
    private final Pose startPose = poseFactory.of(56, 8, 90);

    private final Pose testPose = poseFactory.of(56, 23, 90);
    private final Pose shootPose = poseFactory.of(24,23, 44);
    private final Pose parkPose = poseFactory.of(10, 104, 90);

    // Path methods
    private Path startToTest() {
        return line(startPose, testPose).linear(startPose, testPose);
    }
    private Path testToShoot() {
        return line(testPose, shootPose).linear(testPose, shootPose);
    }

    private Path park(){
        return line(shootPose, parkPose).linear(shootPose, parkPose);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, startToTest()),
                follow(follower, testToShoot()),
                // Add mechanism commands here.
                follow(follower, park())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
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