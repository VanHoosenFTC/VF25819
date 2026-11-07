package org.firstinspires.ftc.teamcode.teleop;


import static com.pedropathing.ivy.commands.Commands.*;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.JoeRobot;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;


import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Teleop")
public class Teleop extends NextOpMode {
    private final JoeRobot joeRobot;

    public Teleop(JoeRobot joeRobot) {
        super(joeRobot);
        this.joeRobot = joeRobot;
        Telemetry.addBackend(PanelsTelemetry.INSTANCE.getFtcTelemetry());

        Scheduler.reset();
    }

    @Override
    public void start() {
        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);


        joeRobot.init().schedule();

        joeRobot.startDrive(gamepad1);

        //gp1.leftBumper().onTrue(instant(()-> joeRobot.getIntake().cycle()));
        //gp1.rightBumper().onTrue(joeRobot.launch());

//        gp1.dpadUp().onTrue(joeRobot.getLauncher().setPollen());
//        gp1.dpadDown().onTrue(joeRobot.getLauncher().setNectar());

//        gp1.a().onTrue(joeRobot.getIntakeWheels().forward(1));
//        gp1.a().onFalse(joeRobot.getIntakeWheels().forward(0));
//        gp1.a().onTrue(joeRobot.getIntake().forward());
//        gp1.a().onFalse(joeRobot.getIntake().stop());
//        gp1.x().onTrue(joeRobot.getIntake().reverse());

        gp1.rightTrigger().isOver(.4).whileTrue(joeRobot.getIntake().forward());
        gp1.rightTrigger().isUnder(.4).whileTrue(joeRobot.getIntake().stop());

        gp1.leftTrigger().isOver(.4).whileTrue(joeRobot.getIntake().reverse());
        gp1.leftTrigger().isUnder(.4).whileTrue(joeRobot.getIntake().stop());

        joeRobot.startTurret(gp2);

        gp2.rightBumper().onTrue(joeRobot.getFlipper().open());
        gp2.rightBumper().onFalse(joeRobot.getFlipper().close());

        gp2.rightTrigger().isOver(.4).whileTrue(joeRobot.launch());
        gp2.rightTrigger().isUnder(.4).whileTrue(joeRobot.stopLauncher());

        // Driver selection for the 4 sets of tags using the D-Pad
//        gp2.dpadUp().onTrue(instant(() -> joeRobot.getVision().setTarget(Vision.TargetTag.TAG_1)));
//        gp2.dpadDown().onTrue(instant(() -> joeRobot.getVision().setTarget(Vision.TargetTag.TAG_2)));
//        gp2.dpadLeft().onTrue(instant(() -> joeRobot.getVision().setTarget(Vision.TargetTag.TAG_3)));
//        gp2.dpadRight().onTrue(instant(() -> joeRobot.getVision().setTarget(Vision.TargetTag.TAG_4)));

        gp2.dpadUp().onTrue(joeRobot.getTurretRamp().goForward());
        gp2.dpadDown().onTrue(joeRobot.getTurretRamp().goBackwards());
        gp2.dpadLeft().onTrue(joeRobot.getTurretRamp().goHome());


        gp2.rightBumper().onTrue(joeRobot.getLauncher().increaseSpeed());
        gp2.leftBumper().onTrue(joeRobot.getLauncher().decreaseSpeed());

////        gp2.y().onTrue(joeRobot.getLauncher().shoot());
////        gp2.y().onFalse(joeRobot.getLauncher().stopLauncher());
//        //gp2.rightTrigger().isOver(.5).onTrue(joeRobot.getLauncher().shoot());
        // Preset positioning commands interrupt manual control;
        // when they finish via .until(), the scheduler automatically resumes getDefaultCommand()
        gp2.x().onTrue(joeRobot.getTurret().goLeft());
        gp2.a().onTrue(joeRobot.getTurret().goHome());
        gp2.b().onTrue(joeRobot.getTurret().goRight());
//
//        // While left bumper is held, trackTarget runs; releasing it resumes default manual control
//        gp2.leftBumper().whileTrue(joeRobot.getTurret().trackTarget(joeRobot.getVision()));

/*
        gp2.dpadUp().onTrue(hazmatRobot.getLauncher().incrementPower());
        gp2.dpadDown().onTrue(hazmatRobot.getLauncher().decrementPower());*/

//        gp2.circle().onTrue(joeRobot.getTransfer().open());
//        gp2.square().onTrue(joeRobot.getTransfer().close());

    }

    @Override
    public void periodic() {
//        telemetry.addData("Intake Speed", joeRobot.getIntake().getSpeed());
//        telemetry.addData("Lift Position", joeRobot.getLift().getPos());
//        telemetry.addData("Bucket Position", joeRobot.getTransfer().getRampServo().getPosition());
        Telemetry.log("Launcher Motor Velocity", joeRobot.getLauncher().getLauncherMotor().getEncoderVelocity().into(RotationsPerMinute));
        Telemetry.log("Turret current position", joeRobot.getTurret().getTurretMotor().getEncoderPosition().into(Degrees));
        //Telemetry.update();
    }

    @Override
    public void end() {
    }
}