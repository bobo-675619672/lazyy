package util.thread;

/**
 * 线程池枚举
 */
public enum LifeCyleStatus
{
  NEW,  RUNNING,  STOPPED,  PAUSED,  TERMINATED;
  
  private LifeCyleStatus() {}
}