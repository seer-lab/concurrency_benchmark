package ca.replicatedworkers;

public interface Collection {
  public abstract int size();
  public abstract Object take();
  public abstract boolean add(Object o);
}


