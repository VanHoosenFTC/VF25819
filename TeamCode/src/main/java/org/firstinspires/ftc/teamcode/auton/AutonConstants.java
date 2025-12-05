package org.firstinspires.ftc.teamcode.auton;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class AutonConstants {
    public static double BackLauncherPercent = .75;
    public static double TopLauncherPercent = .65;

    public static Pose blueFrontScorePose = new Pose(54, 84, Math.toRadians(310));
    public static Pose blueFrontStartPose = new Pose(30, 136, Math.toRadians(270));
    public static Pose blueFrontEndPose = new Pose(30, 48, Math.toRadians(180));
    public static Pose blueFrontPickUpOneStage = new Pose(42, 74, Math.toRadians(180));
    public static Pose blueFrontPickUpOne = new Pose(11, 74, Math.toRadians(180));
    public static Pose blueFrontPickUpTwoStage = new Pose(42, 50, Math.toRadians(180));
    public static Pose blueFrontPickUpTwo = new Pose(13, 50, Math.toRadians(180));
    public static Pose redFrontStartPose = new Pose(114, 136, Math.toRadians(270));
    public static Pose redFrontScorePose = new Pose(86, 74, Math.toRadians(230));
    public static Pose redFrontEndPose = new Pose(96, 48, Math.toRadians(180));
    public static Pose redFrontSafePose = new Pose(127, 108, Math.toRadians(180));

    public static Pose redFrontPickUpOneStage = new Pose(85, 96, Math.toRadians(0));
    public static Pose redFrontPickUpOne = new Pose(110, 96, Math.toRadians(0));
    public static Pose redFrontPickUpTwoStage = new Pose(85, 72, Math.toRadians(0));
    public static Pose redFrontPickUpTwo = new Pose(110, 72, Math.toRadians(0));
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

    public static Pose blueFrontSafepose = new Pose(12, 115, Math.toRadians(0));


    public static Pose redBackSafePose = new Pose(108, 13, Math.toRadians(270));

}