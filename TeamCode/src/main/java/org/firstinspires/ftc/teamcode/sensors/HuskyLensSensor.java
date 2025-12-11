package org.firstinspires.ftc.teamcode.sensors;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import dev.nextftc.ftc.ActiveOpMode;

@Configurable
public class HuskyLensSensor {
//    public static int READ_PERIOD = 1;

    private HuskyLens huskyLens;

    public void init(HardwareMap hardwareMap) {
        huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");

//        /*
//         * This sample rate limits the reads solely to allow a user time to observe
//         * what is happening on the Driver Station telemetry.  Typical applications
//         * would not likely rate limit.
//         */
//        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);
//
//        /*
//         * Immediately expire so that the first time through we'll do the read.
//         */
//        rateLimit.expire();
//
//        /*
//         * Basic check to see if the device is alive and communicating.  This is not
//         * technically necessary here as the HuskyLens class does this in its
//         * doInitialization() method which is called when the device is pulled out of
//         * the hardware map.  However, sometimes it's unclear why a device reports as
//         * failing on initialization.  In the case of this device, it's because the
//         * call to knock() failed.
//         */
//        if (!huskyLens.knock()) {
//            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
//        } else {
//            telemetry.addData(">>", "Press start to continue");
//        }
//
//        /*
//         * The device uses the concept of an algorithm to determine what types of
//         * objects it will look for and/or what mode it is in.  The algorithm may be
//         * selected using the scroll wheel on the device, or via software as shown in
//         * the call to selectAlgorithm().
//         *
//         * The SDK itself does not assume that the user wants a particular algorithm on
//         * startup, and hence does not set an algorithm.
//         *
//         * Users, should, in general, explicitly choose the algorithm they want to use
//         * within the OpMode by calling selectAlgorithm() and passing it one of the values
//         * found in the enumeration HuskyLens.Algorithm.
//         *
//         * Other algorithm choices for FTC might be: OBJECT_RECOGNITION, COLOR_RECOGNITION or OBJECT_CLASSIFICATION.
//         */
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

    }

    public boolean tagFound() {
        HuskyLens.Block[] blocks = huskyLens.blocks();
        //ActiveOpMode.telemetry().addData("Block count", blocks.length);
        for (HuskyLens.Block block : blocks) {
            //ActiveOpMode.telemetry().addData("Block", blocks[i].toString());
            /*
             * Here inside the FOR loop, you could save or evaluate specific info for the currently recognized Bounding Box:
             * - blocks[i].width and blocks[i].height   (size of box, in pixels)
             * - blocks[i].left and blocks[i].top       (edges of box)
             * - blocks[i].x and blocks[i].y            (center location)
             * - blocks[i].id                           (Color ID)
             *
             * These values have Java type int (integer).
             */
            if (block.id == 1 || block.id == 5) {
                return true;
            }
        }
        return false;
    }
}
