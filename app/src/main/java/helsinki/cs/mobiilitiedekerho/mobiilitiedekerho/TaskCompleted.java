package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

/**
* Interface to be used for being able to do things in an activity after the task in an AsyncTask has completed.
*/
public interface TaskCompleted {
    void taskCompleted(String response);
}