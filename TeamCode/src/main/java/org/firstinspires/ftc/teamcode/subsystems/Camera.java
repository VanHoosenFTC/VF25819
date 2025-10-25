package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class Camera implements Subsystem {
    public static final Camera INSTANCE = new Camera();

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    private Camera() {
    }

    @Override
    public void initialize() {
        aprilTag = new AprilTagProcessor.Builder().build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(ActiveOpMode.hardwareMap().get(WebcamName.class, "Webcam 1"));
        builder.setCamera(BuiltinCameraDirection.BACK);
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }
//    public Command scan = new InstantCommand(() -> {
//        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//        for (AprilTagDetection detection : currentDetections) {
//            if (detection.metadata != null) {
//                if (detection.metadata.id ==24){
//                    ActiveOpMode.telemetry().addLine("Tag 24 Found");
//                    Light.INSTANCE.power(1.0);
//                } else {
//                    Light.INSTANCE.power(0);
//                }
//            }
//
//        }
//    }).requires(this);




    @Override
    public void periodic() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        ActiveOpMode.telemetry().addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                ActiveOpMode.telemetry().addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                ActiveOpMode.telemetry().addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                ActiveOpMode.telemetry().addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                ActiveOpMode.telemetry().addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                if (detection.id ==24) {
                    ActiveOpMode.telemetry().addLine("Tag 24 Found");
                    Light.INSTANCE.power(1.0);
                } else {
                    Light.INSTANCE.power(0);
                }
            } else {
                ActiveOpMode.telemetry().addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                ActiveOpMode.telemetry().addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        ActiveOpMode.telemetry().addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        ActiveOpMode.telemetry().addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        ActiveOpMode.telemetry().addLine("RBE = Range, Bearing & Elevation");
    }
}
