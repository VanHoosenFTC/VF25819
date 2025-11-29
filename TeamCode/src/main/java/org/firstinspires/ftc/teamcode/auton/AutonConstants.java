package org.firstinspires.ftc.teamcode.auton;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class AutonConstants {
    public static double BackLauncherPercent = .77;
    public static double TopLauncherPercent = .68;

    public static Pose blueFrontScorePose = new Pose(54, 84, Math.toRadians(310));
    public static Pose blueFrontStartPose = new Pose(30, 136, Math.toRadians(270));
    public static Pose blueFrontEndPose = new Pose(30, 48, Math.toRadians(180));
    public static Pose blueFrontPickUpOneStage = new Pose(42, 74, Math.toRadians(180));
    public static Pose blueFrontPickUpOne = new Pose(13, 74, Math.toRadians(180));
    public static Pose blueFrontPickUpTwoStage = new Pose(42, 55, Math.toRadians(180));
    public static Pose blueFrontPickUpTwo = new Pose(13, 55, Math.toRadians(180));
    public static Pose redFrontStartPose = new Pose(114, 136, Math.toRadians(270));
    public static Pose redFrontScorePose = new Pose(87, 72, Math.toRadians(230));
    public static Pose redFrontEndPose = new Pose(96, 48, Math.toRadians(180));

    public static Pose redFrontPickUpOneStage = new Pose(85, 98, Math.toRadians(0));
    public static Pose redFrontPickUpOne = new Pose(110, 98, Math.toRadians(0));
    public static Pose redFrontPickUpTwoStage = new Pose(85, 74, Math.toRadians(0));
    public static Pose redFrontPickUpTwo = new Pose(110, 74, Math.toRadians(0));
    public static Pose redBackStartPose = new Pose(114, 24, Math.toRadians(270));
    public static Pose redBackScorePose = new Pose(87, 88, Math.toRadians(310));
    public static Pose redBackEndPose = new Pose(96, 112, Math.toRadians(180));

    public static Pose redBackPickUpOneStage = new Pose(85, 56, Math.toRadians(0));
    public static Pose redBackPickUpOne = new Pose(110, 56, Math.toRadians(0));
    public static Pose redBackPickUpTwoStage = new Pose(85, 80, Math.toRadians(0));
    public static Pose redBackPickUpTwo = new Pose(110, 80, Math.toRadians(0));
    public static Pose blueBackStartPose = new Pose(56.5, 10, Math.toRadians(270));
    public static Pose blueBackScorePose = new Pose(59, 17, Math.toRadians(292));
    public static Pose blueBackEndPose = new Pose(48, 50, Math.toRadians(0));

    public static Pose blueBackPickUpOneStage = new Pose(40, 27, Math.toRadians(180));
    public static Pose blueBackPickUpOne = new Pose(11, 27, Math.toRadians(180));
    public static Pose blueBackPickUpTwoStage = new Pose(40, 47, Math.toRadians(180));
    public static Pose blueBackPickUpTwo = new Pose(12, 47, Math.toRadians(180));


}