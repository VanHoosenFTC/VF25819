package org.firstinspires.ftc.teamcode.teleop;


import static com.pedropathing.ivy.commands.Commands.instant;

import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.JoeRobot;


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
        gp1.rightBumper().onTrue(joeRobot.launch());

        gp1.dpadUp().onTrue(joeRobot.getLauncher().setPollen());
        gp1.dpadDown().onTrue(joeRobot.getLauncher().setNectar());

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
        telemetry.addData("Launcher Motor Velocity", joeRobot.getLauncher().getLauncherMotor().getEncoderVelocity().into(RotationsPerMinute));

        telemetry.update();
    }

    @Override
    public void end() {
    }
}