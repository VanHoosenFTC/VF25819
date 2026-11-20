package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.mechanisms.Light;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.FlowerPicker;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.triggers.CommandGamepad;
@Configurable
public class JoeRobot implements NextRobot {

    private  Follower follower;
    private Alliance alliance;

    private boolean wasAimed = false;

//    private final Vision vision = new Vision();
    private final Launcher launcher = new Launcher();
    private final Light light = new Light();
    public FlowerPicker getFlowerPicker() {
        return flowerPicker;
    }

    private final FlowerPicker flowerPicker = new FlowerPicker();

    private final Turret turret = new Turret();

    private final Drivetrain drivetrain = new Drivetrain();

    private final Flipper flipper = new Flipper();

    private final Vision vision = new Vision();

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

    private static long launcherWait = 2000;
    private static long indexDelay = 300;

    public JoeRobot(){}

    public void updateAimIndicator() {
        boolean isAimed = (alliance != null) && vision.isAimedAtAllianceHiveCenter(alliance, 3.0);

        if (isAimed != wasAimed) {
            wasAimed = isAimed;
            if (isAimed) {
                light.setColor(Light.GREEN).schedule();
            } else {
                light.setColor(alliance == Alliance.BLUE ? Light.BLUE : Light.RED).schedule();
            }
        }
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public Turret getTurret() {
        return turret;
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

    public void startTurret(CommandGamepad gamepad2) {
        getTurret().setGamepad(gamepad2);
        getTurret().getDefaultCommand().schedule();
    }

    public Command init(){
        return parallel(
                flowerPicker.up(),
                sequential(
                        flipper.close(),
                        flipper.open(),
                        flipper.close()
                ),
                sequential(
                        light.setColor(Light.RED),
                        waitMs(200),
                        light.setColor(Light.ORANGE),
                        waitMs(200),
                        light.setColor(Light.YELLOW),
                        waitMs(200),
                        light.setColor(Light.GREEN),
                        waitMs(200),
                        light.setColor(Light.BLUE),
                        waitMs(200),
                        light.setColor(Light.RED)
                )
        );
    }

    /**
     * Helper command that opens the flipper when the launcher is at velocity,
     * or toggles the flipper (close -> open) when velocity drops, looping continuously.
     */
    private Command ensureLauncherReadyAndFeed() {
        return sequential(
                waitUntil(launcher::isAtVelocity), // Wait until speed recovered
                flipper.open(),                     // Open flipper to feed ring/element
                waitMs(100),                        // Short delay for mechanism positioning
                waitUntil(() -> !launcher.isAtVelocity()), // Wait until launcher loses speed from firing
                flipper.close(),                    // Close flipper to hold next piece
                waitMs(100),
                // Recursively repeat the check/cycle while launch is active
                lazy(this::ensureLauncherReadyAndFeed)
        );
    }

//    public Command launch() {
//        return parallel(
//                launcher.shoot(),
//                intake.idle(),
//                ensureLauncherReadyAndFeed()
//        );
//    }

    public Command launch(){
        return sequential(
                launcher.shoot(),
                waitUntil(launcher::isAtVelocity),
                flipper.open(),
                intake.idle()

        );

    }

    public Command reverseLaunch(){
        return sequential(
                flipper.open(),
                waitMs(200),
                launcher.reverse(),
                intake.reverse()

        );
    }


    public Command stopLauncher(){
        return sequential(
                launcher.stopLauncher(),
                flipper.close(),
                intake.stop()
        );
    }
    /**
     * Auto-tracks the alliance Hive centerline while pre-spinning the launcher flywheel.
     * The flipper stays closed until launch() is triggered.
     */
    public Command trackAndWarmup() {
        return parallel(
                turret.trackTarget(vision, this::getAlliance),
                launcher.shoot()
        );
    }



    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(launcher,drivetrain, flipper, turret, vision, intake, flowerPicker, light);
    }

}