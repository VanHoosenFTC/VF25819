package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.LEFT_REAR_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_FRONT_MOTOR_NAME;
import static org.firstinspires.ftc.teamcode.ChassisConstants.RIGHT_REAR_MOTOR_NAME;

import org.firstinspires.ftc.teamcode.ShootingPosition;
import org.firstinspires.ftc.teamcode.auton.AutonConstants;
import org.firstinspires.ftc.teamcode.sensors.HuskyLensSensor;
import org.firstinspires.ftc.teamcode.sensors.KickstandColorSensor;
import org.firstinspires.ftc.teamcode.sensors.ArtifactColorSensor;
import org.firstinspires.ftc.teamcode.subsystems.KickStand;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Light;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;

public abstract class AbstractDriveTeleOp extends NextFTCOpMode {
    protected final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.LEFT).zeroed();
    // change the names and directions to suit your robot
    protected final MotorEx frontLeftMotor = new MotorEx(LEFT_FRONT_MOTOR_NAME).reversed();
    protected final MotorEx frontRightMotor = new MotorEx(RIGHT_FRONT_MOTOR_NAME);
    protected final MotorEx backLeftMotor = new MotorEx(LEFT_REAR_MOTOR_NAME).reversed();
    protected final MotorEx backRightMotor = new MotorEx(RIGHT_REAR_MOTOR_NAME);

    protected KickstandColorSensor kickstandColorSensor = new KickstandColorSensor();
    protected ArtifactColorSensor artifactColorSensor = new ArtifactColorSensor();
    protected HuskyLensSensor huskyLensSensor = new HuskyLensSensor();

    private boolean autoLaunchingEnabled = true;

    public AbstractDriveTeleOp() {
        addComponents(
                new SubsystemComponent(LauncherSubsystem.INSTANCE,
                        IntakeSubsystem.INSTANCE,
                        Light.INSTANCE,
                        KickStand.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        Launcher.setPowerFactor(AutonConstants.TopLauncherPercent);
        Command driverControlled = getDriverControlledCommand();
        driverControlled.schedule();

        Gamepads.gamepad1().rightTrigger().atLeast(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.start);
        Gamepads.gamepad1().rightTrigger().lessThan(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.idle);
        Gamepads.gamepad1().leftTrigger().atLeast(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.reverse);
        Gamepads.gamepad1().leftTrigger().lessThan(0.5).whenBecomesTrue(IntakeSubsystem.INSTANCE.idle);

        Gamepads.gamepad1().start().whenBecomesTrue(new InstantCommand(imu::zero));

        Gamepads.gamepad1().dpadUp().whenBecomesTrue(KickStand.INSTANCE.travel);
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(KickStand.INSTANCE.park);
        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(KickStand.INSTANCE.raise);
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(KickStand.INSTANCE.lower);

        Gamepads.gamepad1().leftBumper().whenBecomesTrue(LauncherSubsystem.INSTANCE.stopLauncherAndIntakeAndCloseGate());

        //controls to manually control the launcher
        Gamepads.gamepad2().leftStickY().atLeast(0.5).whenBecomesTrue(Launcher.INSTANCE.start);
        Gamepads.gamepad2().leftStickY().lessThan(-0.5).whenBecomesTrue(Launcher.INSTANCE.eject);
        Gamepads.gamepad2().leftStickY().inRange(-0.5, 0.5).whenBecomesTrue(Launcher.INSTANCE.stop);


        Gamepads.gamepad2().rightBumper().whenBecomesTrue(LauncherSubsystem.INSTANCE.adjustPowerFactor(0.01));
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(LauncherSubsystem.INSTANCE.adjustPowerFactor(-0.01));


        Gamepads.gamepad2().y().whenBecomesTrue(LauncherSubsystem.INSTANCE.setShootingPositionAndWarmup(ShootingPosition.TOP));
        Gamepads.gamepad2().b().whenBecomesTrue(LauncherSubsystem.INSTANCE.setShootingPositionAndWarmup(ShootingPosition.BACK));
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(Launcher.INSTANCE.stop);


        Gamepads.gamepad2().rightTrigger().atLeast(0.5).whenBecomesTrue(LauncherSubsystem.INSTANCE.launchContinuous());
        Gamepads.gamepad2().rightTrigger().lessThan(0.5).whenBecomesTrue(LauncherSubsystem.INSTANCE.closeGateAndIdle());

        Gamepads.gamepad2().leftTrigger().atLeast(0.5).whenBecomesTrue(Gate.INSTANCE.reverse);

        Gamepads.gamepad2().start().whenBecomesTrue(new InstantCommand(() -> {
            autoLaunchingEnabled = !autoLaunchingEnabled;
        }));
        // Gamepads.gamepad2().leftTrigger().lessThan(0.5).whenBecomesTrue(Gate.INSTANCE.close);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        ActiveOpMode.telemetry().addData("Auto Launcher Enabled", autoLaunchingEnabled);

        // initialize light off
        Light.INSTANCE.setColor(Light.OFF).schedule();

        // if the april tag is detected and the launcher is near goal make the light green, if the
        // april tag is not detected, but the launcher is near goal make the light yellow, if the
        // april tag is detected, but the launcher is not near goal make the light orange, if both
        // are undetected do nothing
        boolean launcherNearGoal = Launcher.INSTANCE.nearGoal();
        boolean aprilTagFound = huskyLensSensor.tagFound();
        ActiveOpMode.telemetry().addData("April Tag Found", aprilTagFound);
        ActiveOpMode.telemetry().addData("Launcher Near Goal", launcherNearGoal);
        if (aprilTagFound && launcherNearGoal) {
            Light.INSTANCE.setColor(Light.GREEN).schedule();
        } else if (aprilTagFound) {
            Light.INSTANCE.setColor(Light.ORANGE).schedule();
        } else if (launcherNearGoal) {
            Light.INSTANCE.setColor(Light.RED).schedule();
        }

        // Detect end game lines for kickstand
        KickstandColorSensor.DetectedColor kickstandColor = kickstandColorSensor.getDetectedColor();
        ActiveOpMode.telemetry().addData("Kickstand detected color", kickstandColor);
        if (kickstandColor == KickstandColorSensor.DetectedColor.RED) {
            Light.INSTANCE.setColor(Light.RED).schedule();
        } else if (kickstandColor == KickstandColorSensor.DetectedColor.BLUE) {
            Light.INSTANCE.setColor(Light.BLUE).schedule();
        }

        // detect artifact in launch ramp and run launcher if found
        if (autoLaunchingEnabled) {
            boolean artifactFound = artifactColorSensor.artifactFound();
            ActiveOpMode.telemetry().addData("Artifact detected", artifactFound);
            if (artifactFound) {
                LauncherSubsystem.INSTANCE.warmup.schedule();
            } else {
                LauncherSubsystem.INSTANCE.stopLauncher().schedule();
            }
        }

        ActiveOpMode.telemetry().update();
    }

    @Override
    public void onInit() {
        super.onInit();
        ActiveOpMode.telemetry().update();
        Lift.INSTANCE.load.schedule();
        Gate.INSTANCE.open.schedule();
        Gate.INSTANCE.close.schedule();
        KickStand.INSTANCE.travel.schedule();
        Light.INSTANCE.setColor(Light.GREEN).schedule();
        kickstandColorSensor.init(hardwareMap);
        artifactColorSensor.init(hardwareMap);
        huskyLensSensor.init(hardwareMap);

    }


    abstract Command getDriverControlledCommand();
}
