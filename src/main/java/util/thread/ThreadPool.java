package util.thread;

import java.util.List;
import java.util.function.Supplier;

public interface ThreadPool {
    void execute(Runnable paramRunnable); // 执行线程

    void executeIntoContext(Supplier<Runnable> worker); // 将执行线程所在的线程请求上下文塞进来

    <T> List<T> executeTasks(Supplier<List<Supplier<T>>> suppliers);
}
