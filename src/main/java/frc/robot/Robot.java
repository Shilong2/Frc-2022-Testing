// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.ctre.phoenix.motorcontrol.can.*;

import java.text.BreakIterator;

import com.ctre.phoenix.motorcontrol.*;


//Shilong adds
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer; 
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // private static final String kDefaultAuto = "Default";
  // private static final String kCustomAuto = "My Auto";
  // private String m_autoSelected;
  // private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final WPI_VictorSPX leftMotorA = new WPI_VictorSPX(2);
  private final WPI_VictorSPX leftMotorB = new WPI_VictorSPX(3);
  private final MotorControllerGroup left = new MotorControllerGroup(leftMotorA, leftMotorB);
  
  private final WPI_VictorSPX rightMotorA = new WPI_VictorSPX(4);
  private final WPI_VictorSPX rightMotorB = new WPI_VictorSPX(5);
  private final MotorControllerGroup right = new MotorControllerGroup(rightMotorA, rightMotorB);

  //private final WPI_VictorSPX intake = new WPI_VictorSPX(1);
  //private final WPI_VictorSPX intakeArm = new WPI_VictorSPX(6);

  //private final DifferentialDrive armMovement = new DifferentialDrive(intakeArm, intakeArm);
  private final DifferentialDrive robotDrive = new DifferentialDrive(left, right);
  private final XboxController driverController = new XboxController(1);
  
  private final Joystick newController = new Joystick(0);

  //private final double intakeSpeed = 0.5;
  double autoStart = 0;
  boolean goForAuto = false;

  /*
  UsbCamera usbCamera = new UsbCamera("USB Camera 0", 0);
  MjpegServer mjpegServer1 = new MjpegServer("serve_USB Camera 0", 6711);

  mjpegServer1.setSource(usbCamera);
  CvSink cvSink = new CvSink("opencv_USB Camera 0");
  cvSink.setSource(usbCamera);

  CvSource outputStream = new CvSource("Blur", PixelFormat.kMJPEG, 640, 480, 30);
  MjpegServer mjpegServer2 = new MjpegServer("serve_Blur", 1182);
  mjpegServer2.setSource(outputStream);
  */


  @Override
  public void robotInit() {
    // m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    // m_chooser.addOption("My Auto", kCustomAuto);
    // SmartDashboard.putData("Auto choices", m_chooser);
    left.setInverted(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    // m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    // System.out.println("Auto selected: " + m_autoSelected);
    
    autoStart = Timer.getFPGATimestamp();
    //goForAuto = SmartDashboard.getBoolean("Go For Auto", false);
  }

  /** This function is called periodicallmy during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // switch (m_autoSelected) {
    //   case kCustomAuto:
    //     // Put custom auto code here
    //     break;
    //   case kDefaultAuto:
    //   default:
    //     // Put default auto code here
    //     break;
    // }
    
    // Shilong Add pratice; too be deleted
    double timeOfAuto = Timer.getFPGATimestamp() - autoStart;
    if (timeOfAuto < 2) {
      right.set(0.2);
      left.set(0.2);
    } else if (timeOfAuto < 4) {
      right.set(0.2);
      left.set(0.2);
    } else {
      right.set(0);
      left.set(0);
    }
    System.out.println(timeOfAuto);
    robotDrive.feed();
    //.feed is a stupid function to give value that remove the motor safety error popup
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() 
  {
     
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() 
  {
    leftMotorA.configOpenloopRamp(0);
    leftMotorB.configOpenloopRamp(0);
    rightMotorA.configOpenloopRamp(0);
    rightMotorB.configOpenloopRamp(0);

    //robotDrive.arcadeDrive(driverController.getRightX(), -driverController.getLeftY());

    double Cx = -driverController.getRightX();
    double Cy = driverController.getLeftY();
    
    double Jx = -newController.getX();
    double Jy = newController.getY();
    double Jz = -newController.getZ();
    //newController.getAxisCount();
    //System.out.println(Jz + ", " + Jz);
    System.out.println(Cx + ", " + Cy);
    System.out.println(Jx + ", " + Jy + ", " + Jz);

    robotDrive.arcadeDrive((Jy + Cy)/2, (Jz + Cx)/2 + (Jx * 0.5));

    //robotDrive.tankDrive(driverController.getRightTriggerAxis(), -driverController.getLeftTriggerAxis);
    /*
    if( driverController.getLeftBumper() ) 
    {
      intake.set( ControlMode.PercentOutput , -intakeSpeed);
    }
    else if( driverController.getRightBumper() )
    {
      intake.set(ControlMode.PercentOutput, intakeSpeed);
    }
    else if( driverController.getAButton() )
    {
      intake.set(ControlMode.PercentOutput, 0);
    }
    */
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
