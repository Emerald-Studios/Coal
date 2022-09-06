package dk.sebsa.coal.tasks;


import dk.sebsa.emerald.Logable;
import dk.sebsa.emerald.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public class ThreadManager extends Logable {
    private static ThreadManager instance;
    private final TaskManager taskManager;

    private final List<TaskThread> threads = new ArrayList<>();
    private final List<TaskThread> avaibleThreads = Collections.synchronizedList(new ArrayList<>());

    public ThreadManager(TaskManager tkm, Logger l) {
        super(l);

        if (instance != null) throw new ExceptionInInitializerError("Instance of Threadmanager Already exists!");
        else instance = this;

        this.taskManager = tkm;
    }

    public void init() {
        log("Starting ThreadManager");

        // Cores Amount
        int totalCores = Runtime.getRuntime().availableProcessors();

        int workers;
        if(totalCores < 3) workers = 1;
        else workers = (totalCores-4)/2;	// This is sure to cause problems
        log(" - Total Cores: " + totalCores);
        log(" - Worker Cores: " + workers);

        // Create Workers
        for(int i = 0; i < workers; i++) {
            String name = "Worker-";
            if(i < 9) name += "0" + (i+1);
            else name += (i+1);

            TaskThread thread = new TaskThread(taskManager, this);
            threads.add(thread);
            avaibleThreads.add(thread);

            thread.setName(name);
            thread.start();
        }

        log("Created worker threads");
    }

    public void assignTask(Task task) {
        if(task == null) return;
        var t = avaibleThreads.get(0);
        t.currentTask = task;
        t.interrupt();
        avaibleThreads.remove(0);
    }

    public void returnThread(TaskThread thread) { avaibleThreads.add(thread); }
    public boolean threadAvaible() { return !avaibleThreads.isEmpty(); }
    public void stop() {
        log("Stopping worker threads");
        for (TaskThread thread : threads) {
            thread.active.set(false);
//			thread.setDaemon(true);
            thread.interrupt();
        }
    }

    public void demonizeThreads() {
        for (TaskThread thread : threads) {
			thread.setDaemon(true);
        }
    }
}
