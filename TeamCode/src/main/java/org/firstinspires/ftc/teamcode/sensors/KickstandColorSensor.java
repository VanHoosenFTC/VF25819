package org.firstinspires.ftc.teamcode.sensors;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import dev.nextftc.ftc.ActiveOpMode;
@Configurable
public class KickstandColorSensor {
    private NormalizedColorSensor colorSensor;
    public static float gain = 6f;

    public static NormalizedColor red = new NormalizedColor(0.06f, 0.04f, 0.02f);
    public static NormalizedColor blue = new NormalizedColor(0.02f, 0.04f, 0.06f);
    public enum DetectedColor {
        BLUE,
        RED,
        UNKNOWN
    }

    public void init(HardwareMap hardwareMap) {
        this.colorSensor = hardwareMap.get(NormalizedColorSensor.class, "kickstand-cs");
        this.colorSensor.setGain(gain);
    }

    public DetectedColor getDetectedColor() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normGreen = colors.green / colors.alpha;
        normBlue = colors.blue / colors.alpha;
        ActiveOpMode.telemetry().addData("kickstand red", normRed);
        ActiveOpMode.telemetry().addData("kickstand green", normGreen);
        ActiveOpMode.telemetry().addData("kickstand blue", normBlue);

        if (normRed > red.redValue && normGreen > red.greenValue && normBlue > red.blueValue) {
            return DetectedColor.RED;
        } else if (normRed > blue.redValue && normGreen > blue.greenValue && normBlue > blue.blueValue) {
            return DetectedColor.BLUE;
        }

        return DetectedColor.UNKNOWN;
    }
}
