package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoController;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Servointake implements Subsystem {
    public static final Servointake INSTANCE = new Servointake();
    private double velo;

    private Servointake() { }

    private CRServoEx Servo = new CRServoEx("Servointake");

    public Command suckupballs=new SetPower(Servo, 1).requires(this);
    public Command stop=new SetPower(Servo, 0).requires(this);
}

