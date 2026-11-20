package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("FLM");
        c.frontRightName.set("FRM");
        c.backLeftName.set("BLM");
        c.backRightName.set("BRM");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.xPodOffset.set(-5.5);
        c.yPodOffset.set(-5.5);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.6981275169925577);
                Controller secondaryTranslationalForward = Controller.proportional(0.2579394632179347);
                Controller primaryTranslationalLateral = Controller.proportional(0.5340665505992835);
                Controller secondaryTranslationalLateral = Controller.proportional(0.19732331992537933);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01853500611519578));
                c.brake.set(Controller.proportionalFeedforward(0.01575475519791641));

                c.headingFeedback.set(Controller.proportional(4.788418494783786));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04627027631632892, 0.006888177813199044));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06611588897767068, 0.050701245384677476));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0016982905165058807, 0.001980036217973644));

                c.maxAchievableForwardVelocity.set(56.59285914561174);
                c.maxAchievableStrafeVelocity.set(44.677979259065474);
                c.naturalForwardDeceleration.set(27.273402330058104);
                c.naturalStrafeDeceleration.set(82.2650938824573);
            }
    );
}