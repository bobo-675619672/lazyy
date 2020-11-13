package util.thread;

public abstract interface LifeCyle
{
  public abstract void start(); // 启动
  
  public abstract void stop(); // 停止
  
  public abstract void pause(); // 暂停
  
  public abstract LifeCyleStatus getStatus(); // 获取当前状态
}
