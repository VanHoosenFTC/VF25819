package org.firstinspires.ftc.teamcode.auton;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.JoeRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class ShootPreloadPark extends OpMode {

    private boolean isRedAlliance = true;
    private Follower follower;

    //New Pose and Paths from visulazier go here
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose path1 = poseFactory.of(56, 30, 90);
    private final Pose point2 = poseFactory.of(12, 45, 161.1753);
    private final Pose point3 = poseFactory.of(26.6294, 45.2829, -178.8922);
    private final Pose point4 = poseFactory.of(12.5559, 95.7544, 105.5806);

    public Path path1() {
        return line(start, path1).tangent();
    }

    public Path path2() {
        return line(path1, point2).tangent();
    }

    public Path path3() {
        return line(point2, point3).reverseTangent();
    }

    public Path path4() {
        return line(point3, point4).tangent();
    }
    //New Pose and Paths from visulazier go above here

    private final JoeRobot joeRobot;

    public ShootPreloadPark(JoeRobot joeRobot) {
        this.joeRobot = joeRobot;
    }

    private Command autoRoutine() {
        return sequential(
                // this is where we create our auton sequence
                follow(follower, path1()),
                joeRobot.launch(),
                follow(follower, path2()),
                joeRobot.getIntake().forward(),

                waitMs(2000),
                follow(follower, path3()),
                follow(follower, path4())

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
//        if (gamepad1.x) isRedAlliance = false;
//        if (gamepad1.b) isRedAlliance = true;
//
//        if (!isRedAlliance) {
//            poseFactory = PoseFactory.degrees();
//            poseFactory.mirrorX(70.75);
//        } else {
//            poseFactory = PoseFactory.degrees();
//        }
//
//        start = poseFactory.of(10, 132, 90);
//        park = poseFactory.of(10, 120, -90);

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
