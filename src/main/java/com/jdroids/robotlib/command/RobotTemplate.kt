package com.jdroids.robotlib.command

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
import org.firstinspires.ftc.robotcore.internal.system.AppUtil

/**
 * [RobotTemplate] is a class designed to be a base for team's Robot classes for use as part of the
 * Command system. Each robot should instantiate each subsystem. The Robot class is also a candidate
 * for being made as a singleton, as you only have one robot.
 */
abstract class RobotTemplate {
    private val updateThread = UpdateThread()

    private enum class Mode {
        AUTONOMOUS_INIT,
        TELEOP_INIT,
        RUNNING,
        NONE
    }

    private var mode = Mode.NONE

    /**
     * This function is meant to initialize all of the subsystem's hardware. It is meant to be run
     * at the beginning of the OpMode.
     */
    private fun initHardware() {
        val opModeManager: OpModeManagerImpl =
                OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().activity)

        val subsystems = Scheduler.getSubsystems()

        for (subsystem in subsystems) {
            subsystem.initHardware(opModeManager.activeOpMode.hardwareMap)
        }
    }

    /**
     * The init shadow method
     */
    private fun initShadowMethod() {
        initHardware()
        updateThread.start()
    }

    /**
     * This method is meant to be called at the beginning of each autonomous program, and should do
     * such things as initialize servo positions. If this is overridden, the method should always
     * run `super.autonomousInit`.
     */
    open fun autonomousInit() {
        mode = Mode.AUTONOMOUS_INIT
        initShadowMethod()
    }

    /**
     * This method is meant to be called at the beginning of each teleop program. If this is
     * overridden, the method should always run `super.teleopInit()`. Please keep in mind it is
     * illegal to initialize servo positions in the init phase of teleop.
     */
    open fun teleopInit() {
        mode = Mode.TELEOP_INIT
        initShadowMethod()
    }

    private inner class IsRunningListener : OpModeManagerNotifier, OpModeManagerNotifier.Notifications {
        override fun registerListener(listener: OpModeManagerNotifier.Notifications?): OpMode {
            val opModeManager =
                    OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().activity)
            opModeManager.registerListener(listener)
            return opModeManager.activeOpMode
        }

        override fun unregisterListener(listener: OpModeManagerNotifier.Notifications?) {
            val opModeManager =
                    OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().activity)
            opModeManager.unregisterListener(listener)
        }

        var isOpModeRunning = false
            private set

        override fun onOpModePreInit(opMode: OpMode?) {
            isOpModeRunning = true
        }

        override fun onOpModePreStart(opMode: OpMode?) {
            isOpModeRunning = true
            mode = Mode.RUNNING
        }

        override fun onOpModePostStop(opMode: OpMode?) {
            isOpModeRunning = true
            mode = Mode.NONE
        }
    }

    private inner class UpdateThread : Thread() {
        val opModeManager: OpModeManagerImpl =
                OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().activity)

        val listener = IsRunningListener()

        override fun run() {
            opModeManager.registerListener(listener)
            while (!Thread.currentThread().isInterrupted) {
                val shouldRun = mode == Mode.AUTONOMOUS_INIT || mode == Mode.RUNNING
                if (listener.isOpModeRunning && shouldRun) {
                    if (Scheduler.isEnabled()) {
                        Scheduler.disable()
                    }
                }
                else {
                    if (!Scheduler.isEnabled()) {
                        Scheduler.enable()
                    }
                }
                Scheduler.run()
            }
        }
    }
}