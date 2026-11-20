package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
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

@NextTeleop(name = "Teleop")
public class Teleop extends NextOpMode {
    private final JoeRobot joeRobot;
    private Alliance selectedAlliance = Alliance.RED;
    private boolean hasInitializedHardware = false;

    public Teleop(JoeRobot joeRobot) {
        super(joeRobot);
        this.joeRobot = joeRobot;
        Telemetry.addBackend(PanelsTelemetry.INSTANCE.getFtcTelemetry());

        Scheduler.reset();
    }

    @Override
    public void disabledPeriodic() {
        // Run hardware init sequence once during init
        if (!hasInitializedHardware) {
            joeRobot.init().schedule();
            hasInitializedHardware = true;
        }

        // Press B for Red or X for Blue on Gamepad 1 before pressing Play
        if (gamepad1.x) {
            selectedAlliance = Alliance.BLUE;
        } else if (gamepad1.b) {
            selectedAlliance = Alliance.RED;
        }

        joeRobot.setAlliance(selectedAlliance);

        // Prominent Telemetry check on Driver Station screen during Init
        Telemetry.log("ALLIANCE SETTING", selectedAlliance == Alliance.BLUE ? "🔵 BLUE ALLIANCE" : "🔴 RED ALLIANCE");
        Telemetry.log("Press GP1 X for BLUE | Press GP1 B for RED", "");
    }

    @Override
    public void start() {
        Trigger.Companion.getDefaultEventLoop().clear();

        // Lock in the chosen alliance when TeleOp starts
        joeRobot.setAlliance(selectedAlliance);

        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);

        joeRobot.startDrive(gamepad1);

        // Mid-match alliance failsafe: Hold Back (Select) + X for Blue, or Back (Select) + B for Red
        gp1.back().and(gp1.x()).onTrue(instant(() -> {
            selectedAlliance = Alliance.BLUE;
            joeRobot.setAlliance(Alliance.BLUE);
        }));

        gp1.back().and(gp1.b()).onTrue(instant(() -> {
            selectedAlliance = Alliance.RED;
            joeRobot.setAlliance(Alliance.RED);
        }));

        gp1.rightTrigger().isOver(.4).whileTrue(joeRobot.getIntake().forward());
        gp1.rightTrigger().isUnder(.4).whileTrue(joeRobot.getIntake().stop());

        gp1.leftTrigger().isOver(.4).whileTrue(joeRobot.getIntake().reverse());
        gp1.leftTrigger().isUnder(.4).whileTrue(joeRobot.getIntake().stop());
        gp1.dpadUp().onTrue(joeRobot.getFlowerPicker().up());
        gp1.dpadDown().onTrue(joeRobot.getFlowerPicker().down());

        joeRobot.startTurret(gp2);

        gp2.rightTrigger().isOver(.4).whileTrue(joeRobot.launch());
        gp2.rightTrigger().isUnder(.4).whileTrue(joeRobot.stopLauncher());
        gp2.leftTrigger().isOver(.4).whileTrue(joeRobot.reverseLaunch());
        gp2.leftTrigger().isUnder(.4).whileTrue(joeRobot.stopLauncher());

        gp2.dpadUp().onTrue(joeRobot.getTurretRamp().goForward());
        gp2.dpadDown().onTrue(joeRobot.getTurretRamp().goBackwards());
        gp2.dpadLeft().onTrue(joeRobot.getTurretRamp().goHome());

        gp2.rightBumper().onTrue(joeRobot.getLauncher().increaseSpeed());
        gp2.leftBumper().onTrue(joeRobot.getLauncher().decreaseSpeed());

        gp2.x().onTrue(joeRobot.getTurret().goLeft());
        gp2.a().onTrue(joeRobot.getTurret().goHome());
        gp2.b().onTrue(joeRobot.getTurret().goRight());

        // Hold Y on GP2 to auto-track whichever alliance Hive is in view
        gp2.y().whileTrue(joeRobot.trackAndWarmup());
    }

    @Override
    public void periodic() {
        joeRobot.updateAimIndicator();
        Telemetry.log("Alliance", joeRobot.getAlliance());
        Telemetry.log("Turret current position", joeRobot.getTurret().getTurretMotor().getEncoderPosition().into(Degrees));
    }

    @Override
    public void end() {
    }
}
