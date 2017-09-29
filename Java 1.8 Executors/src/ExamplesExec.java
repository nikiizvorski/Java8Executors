import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by nikiizvorski on 29/09/2017.
 *
 * An Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks.
 */
public class ExamplesExec {

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args){
        standartExec();

        callableExample();

        timeOutExec();

        exampleInvokeSample();

        exampleScheduledExec();
    }

    /**
     * We've already learned how to submit and run tasks once on an executor.
     * In order to periodically run common tasks multiple times, we can utilize scheduled thread pools.
     * A ScheduledExecutorService is capable of scheduling tasks to run either periodically or once after a certain amount of time has elapsed.
     */
    private static void exampleScheduledExec() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());

        ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS);

        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms \n", remainingDelay);

        executor.schedule(task, 3, TimeUnit.SECONDS);

        executor.shutdown();
    }

    /**
     * Executors support batch submitting of multiple callables at once via invokeAll().
     * This method accepts a collection of callables and returns a list of futures.
     * Its a async. But executed in parallel.
     *
     * @throws InterruptedException
     */
    private static void exampleInvokeSample() {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        try {
            executor.invokeAll(callables)
                    .stream()
                    .map(future -> {
                        try {
                            String threadName = Thread.currentThread().getName();
                            System.out.println("Thread " + threadName);
                            return future.get();
                        }
                        catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // don't forget to shutdown the executor
        executor.shutdown();
    }

    /**
     * Any call to future.get() will block and wait until the underlying callable has been terminated.
     * In the worst case a callable runs forever - thus making your application unresponsive.
     * You can simply counteract those scenarios by passing a timeout.
     *
     */
    private static void timeOutExec()  {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println("Thread " + threadName);
                TimeUnit.SECONDS.sleep(2);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        try {
            System.out.println("Result TimeOut: " + future.get(2, TimeUnit.SECONDS).toString());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        // don't forget to shutdown the executor
        executor.shutdown();
    }

    /**
     * The Concurrency API introduces the concept of an ExecutorService as a higher level replacement for working with threads directly.
     * Executors are capable of running asynchronous tasks and typically manage a pool of threads, so we don't have to create new threads manually.
     * All threads of the internal pool will be reused under the hood for revenant tasks, so we can run as many concurrent tasks as
     * we want throughout the life-cycle of our application with a single executor service.
     *
     * empty params
     *
     */
    private static void standartExec() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Thread " + threadName);
        });

        // don't forget to shutdown the executor
        executor.shutdown();
    }

    /**
     *
     * In addition to Runnable executors support another kind of task named Callable.
     * Callables are functional interfaces just like runnables but instead of being void they return a value.
     * Callables can be submitted to executor services just like runnables.
     * But what about the callables result? Since submit() doesn't wait until the task completes,
     * the executor service cannot return the result of the callable directly.
     * Instead the executor returns a special result of type Future which can be used to retrieve the actual result at a later point in time.
     * Futures are tightly coupled to the underlying executor service. Keep in mind that every non-terminated future will throw exceptions if you shutdown the executor.
     */
    private static void callableExample() {
        Callable<Integer> task = () -> {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println("Thread " + threadName);
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        ExecutorService executora = Executors.newFixedThreadPool(1);
        Future<Integer> future = executora.submit(task);

        System.out.println("future done? " + future.isDone());

        Integer result = null;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("future done? " + future.isDone());
        System.out.println("result: " + result);

        // don't forget to shutdown the executor
        executora.shutdown();
    }
}
