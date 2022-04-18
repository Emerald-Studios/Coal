package dk.sebsa.coal.tasks;


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
                currentTask.run();
                taskManager.returnTask(currentTask);
                currentTask = null;
                threadManager.returnThread(this);
            }
        }

        log("Stopping...");
    }
}
