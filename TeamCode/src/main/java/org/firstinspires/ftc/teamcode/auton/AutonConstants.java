package org.firstinspires.ftc.teamcode.auton;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class AutonConstants {
    public static double BackLauncherPercent = .69;
    public static double TopLauncherPercent = .52;

    public static double shootingTime = 1.25;

    public static double transitionWait = 0;

    public static Pose blueFrontScorePose = new Pose(51, 87, Math.toRadians(310));
    public static Pose blueFrontProtoScorePose = new Pose(57, 85, Math.toRadians(315));
    public static Pose blueFrontStartPose = new Pose(30, 136, Math.toRadians(270));
    public static Pose blueFrontProtoStartPose = new Pose(17,117, Math.toRadians(135));
    public static Pose blueFrontEndPose = new Pose(30, 48, Math.toRadians(180));

    public static Pose blueFrontProtoEndPose = new Pose(60, 104, Math.toRadians(180));
    public static Pose blueFrontPickUpOneStage = new Pose(42, 74, Math.toRadians(180));

    public static Pose blueFrontProtoPickUpOneStage = new Pose(56,80, Math.toRadians(180));
    public static Pose blueFrontPickUpOne = new Pose(10, 74, Math.toRadians(180));

    public static Pose blueFrontProtoPickUpOne = new Pose(25,84, Math.toRadians(180));
    public static Pose blueFrontPickUpTwoStage = new Pose(42, 50, Math.toRadians(180));

    public static Pose blueFrontProtoPickUpTwoStage = new Pose(56,58, Math.toRadians(180));
    public static Pose blueFrontPickUpTwo = new Pose(10, 50, Math.toRadians(180));

    public static Pose blueFrontProtoPickUpTwo = new Pose(24,61,Math.toRadians(180));
    public static Pose redFrontStartPose = new Pose(114, 136, Math.toRadians(270));

    public static Pose redFrontProtoStartPose = new Pose(115, 128, Math.toRadians(225));
    public static Pose redFrontScorePose = new Pose(88, 78, Math.toRadians(230));

    public static Pose redFrontProtoScorePose = new Pose(78,104, Math.toRadians(225));
    public static Pose redFrontEndPose = new Pose(96, 48, Math.toRadians(180));

    public static Pose redFrontProtoEndPose = new Pose(87,104, Math.toRadians(0));
    public static Pose redFrontSafePose = new Pose(120, 100, Math.toRadians(180));

    public static Pose redFrontPickUpOneStage = new Pose(85, 83, Math.toRadians(180));
    public static Pose redFrontProtoPickUpOneStage = new Pose(87, 80, Math.toRadians(180));
    public static Pose redFrontPickUpOne = new Pose(118, 104, Math.toRadians(180));

    public static Pose redFrontProtoPickUpOne = new Pose(118, 80, Math.toRadians(180));
    public static Pose redFrontPickUpTwoStage = new Pose(85, 74, Math.toRadians(0));

    public static Pose redFrontProtoPickUpTwoStage = new Pose(87,58, Math.toRadians(0));
    public static Pose redFrontPickUpTwo = new Pose(115, 74, Math.toRadians(0));

    public static Pose redFrontProtoPickUpTwo = new Pose(120,61,Math.toRadians(0));
    public static Pose redBackStartPose = new Pose(87.5, 9, Math.toRadians(270));
    public static Pose redBackScorePose = new Pose(85, 13, Math.toRadians(248));
    public static Pose redBackEndPose = new Pose(88, 36, Math.toRadians(180));

    public static Pose redBackPickUpOneStage = new Pose(85, 46, Math.toRadians(0));
    public static Pose redBackPickUpOne = new Pose(112, 46, Math.toRadians(0));
    public static Pose redBackPickUpTwoStage = new Pose(85, 69, Math.toRadians(0));
    public static Pose redBackPickUpTwo = new Pose(112, 69, Math.toRadians(0));
    public static Pose blueBackStartPose = new Pose(56.5, 10, Math.toRadians(270));
    public static Pose blueBackScorePose = new Pose(59, 17, Math.toRadians(292.5));
    public static Pose blueBackEndPose = new Pose(48, 50, Math.toRadians(0));

    public static Pose bluebackSafePose = new Pose(38, 15, Math.toRadians(270));

    public static Pose blueBackPickUpOneStage = new Pose(40, 25, Math.toRadians(180));
    public static Pose blueBackPickUpOne = new Pose(10, 25, Math.toRadians(180));
    public static Pose blueBackPickUpTwoStage = new Pose(40, 49, Math.toRadians(180));
    public static Pose blueBackPickUpTwo = new Pose(10, 49, Math.toRadians(180));

    public static Pose blueFrontSafepose = new Pose(10, 110, Math.toRadians(0));


    public static Pose redBackSafePose = new Pose(108, 13, Math.toRadians(270));

}