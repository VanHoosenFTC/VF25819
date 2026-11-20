package org.firstinspires.ftc.teamcode.teleop;


import static dev.nextftc.units.Units.Degrees;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.JoeRobot;
import org.firstinspires.ftc.teamcode.data.Alliance;


import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Testing")
public class Testing extends NextOpMode {
    private final JoeRobot joeRobot;

    public Testing(JoeRobot joeRobot) {
        super(joeRobot);
        this.joeRobot = joeRobot;
        Telemetry.addBackend(PanelsTelemetry.INSTANCE.getFtcTelemetry());

        Scheduler.reset();
    }

    @Override
    public void disabledPeriodic() {
        joeRobot.init().schedule();
    }

    @Override
    public void start() {
        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);


        joeRobot.startDrive(gamepad1);
        joeRobot.startTurret(gp2);

        gp2.a().onTrue(joeRobot.getSecondaryIntake().forward());
        gp2.a().onFalse(joeRobot.getSecondaryIntake().stop());

        gp2.b().onTrue(joeRobot.getIntake().forward());
        gp2.b().onFalse(joeRobot.getIntake().stop());

        gp2.rightTrigger().isOver(.4).whileTrue(joeRobot.indexedLaunch());
        gp2.rightTrigger().isUnder(.4).whileTrue(joeRobot.stopIndexedLauncher());

        gp2.leftTrigger().isOver(.4).whileTrue(joeRobot.reverseIndexedLaunch());
        gp2.rightTrigger().isUnder(.4).whileTrue(joeRobot.stopIndexedLauncher());

        gp2.dpadUp().onTrue(joeRobot.getFiringIndexer().forward());
        gp2.dpadDown().onTrue((joeRobot.getFiringIndexer().reverse()));

    }

    @Override
    public void periodic() {
        Telemetry.log("Turret current position", joeRobot.getTurret().getTurretMotor().getEncoderPosition().into(Degrees));
        Telemetry.log("Red hive is in scoring position", joeRobot.getVision().isAllianceHiveTiltedUp(Alliance.RED));
    }

    @Override
    public void end() {
    }
}