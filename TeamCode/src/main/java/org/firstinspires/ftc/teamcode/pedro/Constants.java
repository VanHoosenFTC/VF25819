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
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.xPodOffset.set(-6.567069226362575);
        c.yPodOffset.set(-5.597332331139271);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });


    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.21673573246597627);
                Controller secondaryTranslationalForward = Controller.proportional(0.0800780618607471);
                Controller primaryTranslationalLateral = Controller.proportional(0.9422513238995138);
                Controller secondaryTranslationalLateral = Controller.proportional(0.3481366867617966);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.018666030678729042));
                c.brake.set(Controller.proportionalFeedforward(0.015866126076919684));

                c.headingFeedback.set(Controller.proportional(3.4459241569791197));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04333768992120032, 0.010806452497216587));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06199817893378212, 0.050418129135617856));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0017202583013053256, 0.0020499811297467444));

                c.maxAchievableForwardVelocity.set(54.110783579053916);
                c.maxAchievableStrafeVelocity.set(42.44155458197613);
                c.naturalForwardDeceleration.set(38.861658978051345);
                c.naturalStrafeDeceleration.set(58.747237475924365);
            }
    );
}