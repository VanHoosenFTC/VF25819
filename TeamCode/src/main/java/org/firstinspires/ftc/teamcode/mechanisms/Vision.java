package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Vision implements Mechanism {

    private Limelight3A limelight;
    private boolean isInitialized = false;

    public enum TargetTag {
        TAG_1(30),
        TAG_2(33),
        TAG_3(13),
        TAG_4(14);

        public final int id;
        TargetTag(int id) {
            this.id = id;
        }
    }

    private TargetTag currentTarget = TargetTag.TAG_1;

    public Vision() {
        // Constructor remains entirely empty to prevent premature hardware map access
    }

    /**
     * Safely grabs the hardware and starts the Limelight only on the first call.
     */
    private void ensureInitialized() {
        if (!isInitialized) {
            limelight = RobotController.hardwareMap().get(Limelight3A.class, "limelight");
            limelight.setPollRateHz(100);
            limelight.pipelineSwitch(0);
            limelight.start();
            isInitialized = true;
        }
    }

    public void setTarget(TargetTag target) {
        this.currentTarget = target;
    }

    public TargetTag getTarget() {
        return currentTarget;
    }

    public Double getTargetTx() {
        // Ensure hardware is loaded before we attempt to read from it
        ensureInitialized();
        Telemetry.log("getting target");
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
                Telemetry.log("tag found", fiducial.getFiducialId());
                Telemetry.log("looking for tag", currentTarget.id);
                if (fiducial.getFiducialId() == currentTarget.id) {
                    Telemetry.log("found it!!!", currentTarget);
                    Telemetry.log("value of limelight degrees", fiducial.getTargetXDegrees());
                    return fiducial.getTargetXDegrees();
                }
            }
        }
        return null;
    }
}