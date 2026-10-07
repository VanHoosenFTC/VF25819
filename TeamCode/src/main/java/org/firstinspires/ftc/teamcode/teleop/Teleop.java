package org.firstinspires.ftc.teamcode.teleop;


import static com.pedropathing.ivy.commands.Commands.instant;

import static dev.nextftc.units.Units.Degrees;
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
        gp1.a().onTrue(joeRobot.getContinuousServoExample().forward(1.0));
        gp1.a().onFalse(joeRobot.getContinuousServoExample().forward(0));
        gp1.b().whileTrue(joeRobot.getContinuousServoExample().reverse(-1.0));
        gp1.b().onFalse(joeRobot.getContinuousServoExample().forward(0));
        gp1.x().onTrue((joeRobot.getServoExample().open()));
        gp1.x().onFalse(joeRobot.getServoExample().mid());
        gp1.y().onTrue(joeRobot.getServoExample().close());
        gp1.y().onFalse(joeRobot.getServoExample().mid());
        gp1.rightBumper().toggleOnTrue(joeRobot.getFlipper().open());
        gp1.rightBumper().toggleOnFalse(joeRobot.getFlipper().close());

        gp2.x().toggleOnTrue(joeRobot.getTurret().goLeft());
        gp2.a().toggleOnTrue(joeRobot.getTurret().goHome());
        gp2.b().toggleOnTrue(joeRobot.getTurret().goRight());


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
        telemetry.addData("servoExample position", joeRobot.getServoExample().getServo().getPosition());
        telemetry.addData("Turret current position", joeRobot.getTurret().getTurretMotor().getEncoderPosition().into(Degrees));

        telemetry.update();
    }

    @Override
    public void end() {
    }
}