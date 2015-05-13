package ca.replicatedworkers;

import java.util.Vector;

public interface WorkItemComputation {
  public abstract boolean doWork(Vector newWork, Vector results);
}


