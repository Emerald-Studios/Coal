package dk.sebsa.coal.tasks;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class TaskThread extends Thread {
    public AtomicBoolean active = new AtomicBoolean(false);
    public Task currentTask;
    private TaskManager taskManager;
    private ThreadManager threadManager;

    public TaskThread(TaskManager taskManager, ThreadManager threadManager) {
        this.taskManager = taskManager;
        this.threadManager = threadManager;
    }

    protected void log(String s) { ThreadLogging.log(s, "TaskThread"); }

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
                try {
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
}
