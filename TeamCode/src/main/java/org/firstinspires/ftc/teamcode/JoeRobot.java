package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.mechanisms.ContinuousServoExample;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.IntakeWheels;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.mechanisms.TurretRamp;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.triggers.CommandGamepad;

public class JoeRobot implements NextRobot {
    private  Follower follower;
    private Alliance alliance;

//    private final Vision vision = new Vision();
    private final Launcher launcher = new Launcher();

    private final Turret turret = new Turret();

    private final Drivetrain drivetrain = new Drivetrain();

    private final Flipper flipper = new Flipper();

    private final IntakeWheels intakeWheels = new IntakeWheels();

    private final Vision vision = new Vision();
    private final TurretRamp turretRamp = new TurretRamp();

    public Intake getIntake() {
        return intake;
    }

    private final Intake intake = new Intake();

    public Vision getVision() {
        return vision;
    }

    public Flipper getFlipper() {
        return flipper;
    }

    public IntakeWheels getIntakeWheels() {
        return intakeWheels;
    }

    public TurretRamp getTurretRamp() { return turretRamp;}

    private final ContinuousServoExample continuousServoExample = new ContinuousServoExample();

    public JoeRobot(){}


//    public Vision getVision() {
//        return vision;
//    }

    public Launcher getLauncher() {
        return launcher;
    }

    public Turret getTurret() {
        return turret;
    }

    public ContinuousServoExample getContinuousServoExample() { return continuousServoExample;}

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

    public void startTurret(CommandGamepad gamepad2) {
        getTurret().setGamepad(gamepad2);
        getTurret().getDefaultCommand().schedule();
    }

    public Command init(){
        return parallel(
                turretRamp.goHome()
        );
    }

    public Command launch(){
        return sequential(
                launcher.shoot(),
                waitMs(500),
                intake.forward()

        );
    }

    public Command stopLauncher(){
        return sequential(
                launcher.stopLauncher(),
                intake.stop()
        );
    }


    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(launcher,drivetrain, continuousServoExample, flipper, intakeWheels, turret, vision, intake, turretRamp);
    }

}