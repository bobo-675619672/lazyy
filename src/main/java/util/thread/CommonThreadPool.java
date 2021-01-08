package util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class CommonThreadPool implements LifeCyle, ThreadPool {

    private ExecutorService threadPool;
    private int size = 50;
    private LifeCyleStatus status = LifeCyleStatus.NEW;

    private static CommonThreadPool instance = null;

    /**
     * 私有化构造方法
     * 不能new
     */
    private CommonThreadPool() {

    }

    public static CommonThreadPool getInstance() {
        if (null == instance) {
            instance = new CommonThreadPool();
        }
        return instance;
    }

    @Override
    public synchronized void execute(Runnable worker) {
        if (this.status != LifeCyleStatus.RUNNING) {
            start();
        }
        this.threadPool.execute(worker);
    }

    @Override
    public void executeIntoContext(Supplier<Runnable> worker) {
        if (this.status != LifeCyleStatus.RUNNING) {
            start();
        }
        this.threadPool.execute(() -> {
            worker.get();
        });
    }

    @Override
    public <T> List<T> executeTasks(Supplier<List<Supplier<T>>> suppliers) {
        if (this.status != LifeCyleStatus.RUNNING) {
            start();
        }
        CompletionService service = new ExecutorCompletionService(threadPool);
        int i = 0;

        for (Supplier<T> func : suppliers.get()) {
            service.submit(() -> {
                return func.get();
            });
            i++;
        }
        List<T> result = new ArrayList<>(i);
        for (; i > 0; i--) {
            try {
                result.add((T) service.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public synchronized void start() {
        if (this.status != LifeCyleStatus.RUNNING) {
            this.status = LifeCyleStatus.RUNNING;
            this.threadPool = Executors.newFixedThreadPool(this.size);
        }
    }

    @Override
    public synchronized void stop() {
        if (this.status != LifeCyleStatus.RUNNING) {
            return;
        }
        shutdownAndAwaitTermination(this.threadPool);

        this.threadPool = null;

        this.status = LifeCyleStatus.STOPPED;
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LifeCyleStatus getStatus() {
        return this.status;
    }

    public synchronized void setSize(int size) {
        this.size = size;
    }

    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(4L, TimeUnit.SECONDS)) {
                pool.shutdownNow();

                pool.awaitTermination(4L, TimeUnit.SECONDS);
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
