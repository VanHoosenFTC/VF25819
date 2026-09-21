package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.drive.DriveCommands;

public class JoeRobot implements NextRobot {
    private  Follower follower;
    private Alliance alliance;

//    private final Vision vision = new Vision();
    private final Launcher launcher = new Launcher();
    private final Drivetrain drivetrain = new Drivetrain();

    public JoeRobot(){}


//    public Vision getVision() {
//        return vision;
//    }

    public Launcher getLauncher() {
        return launcher;
    }

    public void setAlliance(Alliance alliance){
        this.alliance = alliance;
    }

    public Alliance getAlliance(){
        return alliance;
    }

    public Follower getFollower() {
        if (follower == null) {
            follower = Constants.create(RobotController.hardwareMap());
        }

        return follower;
    }

    public void startDrive(Gamepad gamepad1) {
        DriveCommands.mecanumDrive(
                drivetrain.frontLeft,
                drivetrain.frontRight,
                drivetrain.backLeft,
                drivetrain.backRight,
                gamepad1
        ).schedule();
    }

    public Command init(){
        return parallel(
            launcher.setPollen()
        );
    }

    public Command launch(){
        return sequential(
            launcher.setPoint1(),
            waitMs(2000),
            launcher.stopLauncher()

        );
    }


    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(launcher,drivetrain);
    }

}