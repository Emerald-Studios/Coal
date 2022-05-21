package dk.sebsa.coal.tasks;


import dk.sebsa.Coal;
import dk.sebsa.coal.math.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author sebs
 * @since 1.0.0
 */
public class TaskManager {
    private final List<Task> toDo = new ArrayList<>();
    private final List<Task> doing = Collections.synchronizedList(new ArrayList<>());

    public void doTask(Task t) {
        toDo.add(t);
    }

    public Task getTask() {
        var r = toDo.get(0);
        doing.add(r);
        r.startTime = Time.getTime();

        toDo.remove(0);
        return r;
    }

    public void returnTask(Task task) {
        doing.remove(task);
    }

    public boolean stuffToDo() {
        return !toDo.isEmpty() || !doing.isEmpty();
    }

    public void frame(ThreadManager thm) {
        while(thm.threadAvaible() && !toDo.isEmpty() ) {
            thm.assignTask(getTask());
        }

        // Check if tasks take to long
        if(!Coal.DEBUG) return;
        try {
            for (Task t : doing) {
                var tt = TimeUnit.SECONDS.convert(Time.getTime() - t.startTime, TimeUnit.MILLISECONDS);
                if (tt > 12) {
                    t.startTime = Time.getTime();
                    Coal.logger.error("Task " + t + ", has taken more then 12 seconds to run!");
                    Coal.shutdownDueToError();
                    doing.clear();
                    Coal.instance.getThreadManager().demonizeThreads();
                } else if (tt == 1) {
                    Coal.logger.error("Task " + t + ", has taken 1 second to run!");
                }
            }
        } catch (Exception e) { }
    }
}
