## Welcome!
This GitHub repository is the home for Van Hoosen middle schools FTC robotics programming teams.  It is intended to help our teams learn about FTC programming and maybe more!  This is forked from the main [FtcRobotController](https://github.com/FIRST-Tech-Challenge/FtcRobotController) and includes both the [NextFTC Command Library](https://nextftc.dev/) and the [Pedro Pathing Library](https://pedropathing.com/) libraries.  It also includes a sample robot centric teleop to pull all of this together.  Future updates intend to include a sample autonomous using the command based structure and pathing.

## Requirements
To use this Android Studio project, you will need Android Studio Narwhal or later.  Get Android Studio [here](https://developer.android.com/studio?utm_source=android-studio).

You will also need to use GitHub to clone this repository.  You can use GitHub Desktop (get it [here](https://desktop.github.com/)) or the command line.  If you are new to GitHub, check out [this guide](https://docs.github.com/en/get-started/quickstart/set-up-git).

You should probably be using AI, professionals do, and you will be more productive if you do.  However, this doesn't mean you should you autocomplete everything the tools suggest.  AI is a tool to help you, not replace your thought and expertise.  GitHub Copilot is great and you should have access to it with your GitHub account.  To enable it, follow the following steps:
1. Go to Plugins
   Navigate to File > Settings > Plugins (or Android Studio > Preferences > Plugins on macOS).
2. Search0 for Copilot
   In the Marketplace tab, type GitHub Copilot in the search bar.
3. Install the Plugin
   Click Install next to the GitHub Copilot plugin.
4. Restart Android Studio
   After installation, restart the IDE to activate the plugin.
5. Sign In to GitHub
   When prompted, sign in with your GitHub account to authenticate Copilot.
6. Start Using Copilot
   Copilot suggestions will now appear as you write code in the editor.

## Getting Started
> **Warning:** The school wifi may block some of the dependencies needed to build this project.  If you run into issues where the build fails because it can't download something, try using a different network.


1. Fork this repository for your team.  Forks can be anywhere, but are encouraged to be in the [VanHoosenFTC](https://github.com/VanHoosenFTC) Org in GitHub for Van Hoosen teams.  This will allow us to share code and improvements between teams, and will provide visibility into what other teams are doing.
2. Clone your fork to your local machine using GitHub Desktop or the command line.
3. Open Android Studio and select "Open an existing Android Studio project".  Navigate to the directory where you cloned your fork and select it.
4. Let Android Studio download and install any required SDKs or dependencies.  This may take a few minutes.
5. Make the following changes based on your robot configuration in the TeamCode project:
   - Update `ChassisConstants.java` with your robot's motor configuration.
   - Update 'subsystems/Intake.java' with the name of your intake motor on line 14.
6. Connect your Control Hub to your computer and run the TeamCode Project - you should have a working teleop program that has a simple intake and mecanum drive.  The drive is controlled with Gamepad1 and the intake is controlled with Gamepad2.  For the intake, the x button intakes, the a button stops, and the b button reverses.
7. Proceed to tuning your bot with Pedro Pathing.  Instructions for this are in the [Pedro Pathing Documentation](https://pedropathing.com/docs/pathing/tuning)
   - for the setup, you should only need to update the robots mass in the `pedroPathing/Constants.java` file.
   - The current code is set up for [Two Wheel Localization](https://pedropathing.com/docs/pathing/tuning/localization/two-wheel) so follow that guide for tunning.
   - Once you have tuned your bot, use the Line, Triangle and Circle tests to verify your tuning.