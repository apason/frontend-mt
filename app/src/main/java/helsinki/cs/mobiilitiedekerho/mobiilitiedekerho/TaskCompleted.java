package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

/**
* Interafce to be used for being able to do things in activity after the task in AsyncTask has completed.
*/
public interface TaskCompleted{
    void taskCompleted(String response);
}