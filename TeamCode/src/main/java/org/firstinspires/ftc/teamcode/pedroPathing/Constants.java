package org.firstinspires.ftc.teamcode.pedroPathing;

import static org.firstinspires.ftc.teamcode.ChassisConstants.*;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ChassisConstants;

public class Constants {

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(11.33)
            .forwardZeroPowerAcceleration(-43)
            .lateralZeroPowerAcceleration(-78)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.04,0, 0.005, 0.02))
            //.secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.18, 0, 0.01, 0.025))
            .headingPIDFCoefficients(new PIDFCoefficients(0.7, 0, 0.025, .018))
            //.secondaryHeadingPIDFCoefficients(new PIDFCoefficients(0.75,0, 0.01, 0.23))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.016, 0, 0.0006, 0.6, 0.03))
            ;

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .leftFrontMotorName(LEFT_FRONT_MOTOR_NAME)
            .leftRearMotorName(LEFT_REAR_MOTOR_NAME)
            .rightFrontMotorName(RIGHT_FRONT_MOTOR_NAME)
            .rightRearMotorName(RIGHT_REAR_MOTOR_NAME)
            .leftFrontMotorDirection(LEFT_FRONT_MOTOR_DIRECTION)
            .leftRearMotorDirection(LEFT_REAR_MOTOR_DIRECTION)
            .rightFrontMotorDirection(RIGHT_FRONT_MOTOR_DIRECTION)
            .rightRearMotorDirection(RIGHT_REAR_MOTOR_DIRECTION)
            .xVelocity(57.260)
            .yVelocity(42.379)
            ;

    public static TwoWheelConstants localizerConstants = new TwoWheelConstants()
            .forwardPodY(5.4687)
            .strafePodX(-6.1875)
            .forwardEncoder_HardwareMapName(LEFT_FRONT_MOTOR_NAME)
            .strafeEncoder_HardwareMapName(LEFT_REAR_MOTOR_NAME)
            .strafeEncoderDirection(Encoder.REVERSE)
            .forwardTicksToInches(0.002951)
            .strafeTicksToInches(0.002951)
            .IMU_HardwareMapName("imu")
            .IMU_Orientation(
                    new RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.UP,
                            RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                    )
            );

    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            500,
            1,
            1
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .twoWheelLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}

