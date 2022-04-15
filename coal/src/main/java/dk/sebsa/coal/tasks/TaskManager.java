package dk.sebsa.coal.tasks;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dk.sebsa.Coal;
import dk.sebsa.coal.math.Time;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class TaskManager {
    private List<Task> toDo = new ArrayList<>();
    private List<Task> doing = Collections.synchronizedList(new ArrayList<Task>());

    public void doTask(Task t) {
        toDo.add(t);
    }

    public boolean taskAvaible() { return !toDo.isEmpty(); }
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
            for(int i = 0; i < doing.size(); i++) {
                var t = doing.get(i);
                var tt = TimeUnit.SECONDS.convert(Time.getTime() - t.startTime, TimeUnit.MILLISECONDS);
                if(tt > 8) {
                    t.startTime = Time.getTime();
                    Coal.logger.log("Task " + t + ", has taken more then 8 seconds to run!");
                }
            }
        } catch (Exception e) { }
    }
}
