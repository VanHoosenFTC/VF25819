package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.data.Alliance;

import java.util.Map;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;

@Configurable
public class Vision implements Mechanism {

    private Limelight3A limelight;
    private boolean isInitialized = false;

    // --- TUNABLE TILT DETECTION TOGGLES & THRESHOLDS (Editable live in FTControl Panels) ---

    // Toggle evaluation modes independently
    public static boolean usePitchCheck = true;  // ON by default
    public static boolean useYCheck = false;     // OFF by default

    // Thresholds
    public static double TILT_UP_MIN_PITCH_DEG = 3.0; // Minimum pitch angle in degrees (3D Pose)
    public static double TILT_UP_MAX_TY_DEG = 15.0;    // Maximum vertical angle in degrees (2D ty)

    // Horizontal offsets from Hive centerline in meters:
    public enum TargetGroup {
        RED_SCORING(Alliance.RED, Map.of(
                30, -0.1778,
                31, -0.06985,
                32,  0.06985,
                33,  0.1778
        )),
        RED_AUDIENCE(Alliance.RED, Map.of(
                34, -0.1778,
                35, -0.06985,
                36,  0.06985,
                37,  0.1778
        )),
        BLUE_AUDIENCE(Alliance.BLUE, Map.of(
                38, -0.1778,
                39, -0.06985,
                40,  0.06985,
                41,  0.1778
        )),
        BLUE_SCORING(Alliance.BLUE, Map.of(
                42, -0.1778,
                43, -0.06985,
                44,  0.06985,
                45,  0.1778
        ));

        public final Alliance alliance;
        public final Map<Integer, Double> tagCenterOffsetsMeters;

        TargetGroup(Alliance alliance, Map<Integer, Double> tagCenterOffsetsMeters) {
            this.alliance = alliance;
            this.tagCenterOffsetsMeters = tagCenterOffsetsMeters;
        }
    }

    public Vision() {
    }

    private void ensureInitialized() {
        if (!isInitialized) {
            limelight = RobotController.hardwareMap().get(Limelight3A.class, "limelight");
            limelight.setPollRateHz(100);
            limelight.pipelineSwitch(0);
            limelight.start();
            isInitialized = true;
        }
    }

    /**
     * Checks if the selected alliance's Hive is tilted UP into the active shooting position.
     * Evaluates pitch, Y-axis (ty), or both based on live FTControl Panels configuration toggles.
     */
    public boolean isAllianceHiveTiltedUp(Alliance alliance) {
        ensureInitialized();
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return false;
        }

        // Failsafe: If both check modes are disabled in Panels, fall back to simple tag presence
        boolean verifyPitch = usePitchCheck;
        boolean verifyY = useYCheck;
        if (!verifyPitch && !verifyY) {
            verifyPitch = false;
            verifyY = false;
        }

        for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
            int id = fiducial.getFiducialId();

            // Ignore tags belonging to the opposite alliance
            if (!isTagInAlliance(id, alliance)) {
                continue;
            }

            boolean pitchPassed = true;
            boolean yPassed = true;

            // 1. Evaluate Pitch (3D Pose) if enabled
            if (verifyPitch) {
                Pose3D targetPose = fiducial.getTargetPoseRobotSpace();
                if (targetPose != null && targetPose.getOrientation() != null) {
                    YawPitchRollAngles orientation = targetPose.getOrientation();
                    double pitch = Math.abs(orientation.getPitch(AngleUnit.DEGREES));
                    pitchPassed = pitch >= TILT_UP_MIN_PITCH_DEG;
                } else {
                    pitchPassed = false; // Couldn't resolve 3D pitch pose
                }
            }

            // 2. Evaluate Vertical Y position (ty) if enabled
            if (verifyY) {
                double tagTy = fiducial.getTargetYDegrees();
                yPassed = tagTy <= TILT_UP_MAX_TY_DEG;
            }

            // If selected checks pass for any alliance tag, confirm tilted up
            if (pitchPassed && yPassed) {
                return true;
            }
        }

        return false;
    }

    public Double getHiveCenterTx(TargetGroup group) {
        ensureInitialized();
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return null;
        }

        double sumCenterTx = 0.0;
        int count = 0;

        for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
            int id = fiducial.getFiducialId();
            Double offsetFromCenterMeters = group.tagCenterOffsetsMeters.get(id);

            if (offsetFromCenterMeters != null) {
                double tagTx = fiducial.getTargetXDegrees();
                Pose3D targetPose = fiducial.getTargetPoseRobotSpace();

                double estimatedCenterTx = tagTx;
                if (targetPose != null && targetPose.getPosition() != null) {
                    Position posMeters = targetPose.getPosition().toUnit(DistanceUnit.METER);
                    double planarDistMeters = Math.hypot(posMeters.x, posMeters.y);

                    if (planarDistMeters > 0.1) {
                        double angularCorrectionDeg = Math.toDegrees(Math.atan2(offsetFromCenterMeters, planarDistMeters));
                        estimatedCenterTx = tagTx - angularCorrectionDeg;
                    }
                }

                sumCenterTx += estimatedCenterTx;
                count++;
            }
        }

        return (count > 0) ? (sumCenterTx / count) : null;
    }

    public Double getAllianceHiveCenterTx(Alliance alliance) {
        if (!isAllianceHiveTiltedUp(alliance)) {
            return null;
        }

        Double bestTx = null;
        for (TargetGroup group : TargetGroup.values()) {
            if (group.alliance == alliance) {
                Double groupTx = getHiveCenterTx(group);
                if (groupTx != null && (bestTx == null || Math.abs(groupTx) < Math.abs(bestTx))) {
                    bestTx = groupTx;
                }
            }
        }

        return bestTx;
    }

    private boolean isTagInAlliance(int tagId, Alliance alliance) {
        for (TargetGroup group : TargetGroup.values()) {
            if (group.alliance == alliance && group.tagCenterOffsetsMeters.containsKey(tagId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAimedAtAllianceHiveCenter(Alliance alliance, double toleranceDegrees) {
        Double centerTx = getAllianceHiveCenterTx(alliance);
        return centerTx != null && Math.abs(centerTx) <= toleranceDegrees;
    }
}