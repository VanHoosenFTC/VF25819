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
            .mass(8.89041)
            .forwardZeroPowerAcceleration(-49.397)
            .lateralZeroPowerAcceleration(-59.825)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.2,0, 0.01, 0.02))
            .headingPIDFCoefficients(new PIDFCoefficients(1.3, 0, 0.05, 0.03))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.008, 0, 0.0000001, 0.6, 0.002));

    public static MecanumConstants driveConstants = new MecanumConstants()
            .leftFrontMotorName(LEFT_FRONT_MOTOR_NAME)
            .leftRearMotorName(LEFT_REAR_MOTOR_NAME)
            .rightFrontMotorName(RIGHT_FRONT_MOTOR_NAME)
            .rightRearMotorName(RIGHT_REAR_MOTOR_NAME)
            .leftFrontMotorDirection(LEFT_FRONT_MOTOR_DIRECTION)
            .leftRearMotorDirection(LEFT_REAR_MOTOR_DIRECTION)
            .rightFrontMotorDirection(RIGHT_FRONT_MOTOR_DIRECTION)
            .rightRearMotorDirection(RIGHT_REAR_MOTOR_DIRECTION)
            .xVelocity(61.992)
            .yVelocity(45.712);

    public static TwoWheelConstants localizerConstants = new TwoWheelConstants()
            .forwardPodY(5.4687)
            .strafePodX(-6.1875)
            .forwardEncoder_HardwareMapName(LEFT_FRONT_MOTOR_NAME)
            .strafeEncoder_HardwareMapName(LEFT_REAR_MOTOR_NAME)
            .strafeEncoderDirection(Encoder.REVERSE)
            .forwardTicksToInches(0.00293)
            .strafeTicksToInches(0.00291)
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

