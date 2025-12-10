package org.firstinspires.ftc.teamcode.sensors;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import dev.nextftc.ftc.ActiveOpMode;
@Configurable
public class ArtifactColorSensor {
    private NormalizedColorSensor colorSensor;
    public static float gain = 4f;

    public static double artifactDistance = 2.9;

    public static NormalizedColor greenArtifactColors = new NormalizedColor(0.04f, 0.09f, 0.10f);
    public static NormalizedColor robotgreenColors = new NormalizedColor(0.06f, 0.12f, 0.09f);

    public static NormalizedColor purpleArtifactColors = new NormalizedColor(0.05f, 0.05f, 0.05f);
    public static NormalizedColor yellowArtifactColors = new NormalizedColor(0.01f, 0.07f, 0.03f);

    public void init(HardwareMap hardwareMap) {
        this.colorSensor = hardwareMap.get(NormalizedColorSensor.class, "artifact-cs");
        this.colorSensor.setGain(gain);
    }

    public boolean artifactFound() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normGreen = colors.green / colors.alpha;
        normBlue = colors.blue / colors.alpha;
        ActiveOpMode.telemetry().addData("artifact red", normRed);
        ActiveOpMode.telemetry().addData("artifact green", normGreen);
        ActiveOpMode.telemetry().addData("artifact blue", normBlue);
        ActiveOpMode.telemetry().addData("artifact distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
        boolean purpleFound = false;
        boolean greenFound = false;
        boolean yellowFound = false;
        if (normRed > greenArtifactColors.redValue && normGreen > greenArtifactColors.greenValue && normBlue > greenArtifactColors.blueValue) {
            if (normRed < robotgreenColors.redValue && normGreen < robotgreenColors.greenValue && normBlue < robotgreenColors.blueValue) {
                greenFound = true;
                ActiveOpMode.telemetry().addData("artifact found", "green");
            }
        } else if (normRed > purpleArtifactColors.redValue && normGreen > purpleArtifactColors.greenValue && normBlue > purpleArtifactColors.blueValue) {
            purpleFound = true;
            ActiveOpMode.telemetry().addData("artifact found", "purple");
        } else if (normRed > yellowArtifactColors.redValue && normGreen > yellowArtifactColors.greenValue && normBlue > yellowArtifactColors.blueValue){
            yellowFound = true;
            ActiveOpMode.telemetry().addData("artifact found", "yellow");
        } else {
            ActiveOpMode.telemetry().addData("artifact found", "NONE");
        }

        return greenFound || purpleFound || yellowFound;
    }
}
