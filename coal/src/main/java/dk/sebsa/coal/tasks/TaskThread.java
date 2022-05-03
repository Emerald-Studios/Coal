package dk.sebsa.coal.tasks;


import dk.sebsa.Coal;
import dk.sebsa.coal.audio.AudioManager;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;

/**
 * @author sebs
 * @since 1.0.0
 */
public class TaskThread extends Thread {
    public final AtomicBoolean active = new AtomicBoolean(false);
    public Task currentTask;
    private final TaskManager taskManager;
    private final ThreadManager threadManager;

    public TaskThread(TaskManager taskManager, ThreadManager threadManager) {
        this.taskManager = taskManager;
        this.threadManager = threadManager;
    }

    protected void log(Object s) { ThreadLogging.log(s.toString(), "TaskThread"); }

    @Override
    public void run() {
        log("Started Worker Thread");
        active.set(true);

        while (true) {
            try {
                synchronized (this) {
                    if(currentTask == null) wait();
                }
            } catch (InterruptedException e) {}

            if(!active.get()) break;
            if(currentTask != null) {
                checkAl();
                try {
                    currentTask.thread = this;
                    currentTask.run();
                } catch (Exception | Error e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    log("Task Failed. xD" + sw);
                }

                taskManager.returnTask(currentTask);
                currentTask = null;
                threadManager.returnThread(this);
            }
        }

        log("Stopping...");
    }

    private boolean al = !Coal.getCapabilities().coalAudio;
    public void checkAl() {
        if(al) return;
        log("Set OpenAL capabilities");

        ALC.setCapabilities(AudioManager.getDeviceCaps());
        alcMakeContextCurrent(AudioManager.getContext());
        AL.setCurrentThread(AudioManager.getCapabilities());
        AL.setCurrentProcess(AudioManager.getCapabilities());
        al = true;
    }
}
