# Java8Executors

Why is this project made? 

Main reason is for people to understand how all of the changes in Java 1.8 are done and why. Its an example of the Concurrent API and shows how to use async/sync service. Currently you can turn to Reactive to do parallel and sync/async actions but this is a good base way to know. Since it exists in Android too. And the api is available as far as i know in support too.

An Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks.
An ExecutorService can be shut down, which will cause it to reject new tasks. Two different methods are provided for shutting down an ExecutorService. The shutdown() method will allow previously submitted tasks to execute before terminating, while the shutdownNow() method prevents waiting tasks from starting and attempts to stop currently executing tasks. Upon termination, an executor has no tasks actively executing, no tasks awaiting execution, and no new tasks can be submitted. An unused ExecutorService should be shut down to allow reclamation of its resources.

Method submit extends base method Executor.execute(Runnable) by creating and returning a Future that can be used to cancel execution and/or wait for completion. Methods invokeAny and invokeAll perform the most commonly useful forms of bulk execution, executing a collection of tasks and then waiting for at least one, or all, to complete. (Class ExecutorCompletionService can be used to write customized variants of these methods.)

The Executors class provides factory methods for the executor services provided in this package.

#### Author

- Niki Izvorski (nikiizvorski@gmail.com)
- Oracle
- winterbe
