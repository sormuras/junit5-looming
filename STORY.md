# JUnit 5 & Project Loom

- JUnit 5
- Project Loom
- TestEngine: `junit5-looming`
- TestEngine: `Jupiter` + Loom
- Conclusion

## JUnit 5

Explain concept of `TestEngine`
Platform + Jupiter + Vintage + ...

## Project Loom

A Users' View on Project Loom

Let's read the API documentation of class `java.base/java.lang.Thread` together:
https://download.java.net/java/early_access/loom/docs/api/java.base/java/lang/Thread.html

> A thread is a thread of execution in a program.
> The Java virtual machine allows an application to have multiple threads of execution running concurrently.
>
> Thread supports the creation of threads that are scheduled by the operating system.
> These threads are sometimes known as kernel threads or heavyweight threads and will usually have a large stack and other resources that are maintained by the operating system.
> Kernel threads are suitable for executing all types of tasks but they are a limited resource.
>
> Thread also supports the creation of virtual threads that are scheduled by the Java virtual machine using a small set of kernel threads.
> Virtual threads will typically require few resources and a single Java virtual machine may support millions of virtual threads.
> Virtual threads are suitable for executing tasks that spend most of the time blocked, often waiting for synchronous blocking I/O operations to complete.
> Locking and I/O operations are the scheduling points where a kernel thread is re-scheduled from one virtual thread to another.
> Code executing in virtual threads will usually not be aware of the underlying kernel thread, and in particular, the currentThread() method, to obtain a reference to the current thread, will return the Thread object for the virtual thread.
>
> Thread defines factory methods, and a Thread.Builder API, for creating kernel or virtual threads.
> It also defines (for compatibility and customization reasons) constructors for creating kernel threads.
> Newer code is encouraged to use the factory methods or the builder rather than the constructors.

With this documentation (noted the `@since 1` tag?) in mind, let's also read Ron's mail:
https://mail.openjdk.java.net/pipermail/loom-dev/2019-December/000931.html

>  API
>  ---
>
>  This prototype has the new API we introduced in October, that represents Loom's
>  lightweight user-mode threads as instances of java.lang.Thread [1]; the
>  rationale for that decision is explained in [2]. We are now calling Loom's
>  lightweight user-mode threads "virtual threads" [3].
>
>  Virtual threads can be created with the newly-introduced Thread.Builder class.
>  It can be used to directly build Thread instances, or to create a ThreadFactory
>  instance. For example:
>
>      Thread thread = Thread.builder().virtual().task(() -> { ... }).start();
>
>  Thread.Builder exposes other settings we're experimenting with, like optionally
>  disallowing the use of ThreadLocal.
>
>  The previous EA build introduced structured concurrency [4]. In the updated
>  prototype, a more limited form of a structured concurrency can be achieved with
>  ExecutorService; for example:
>
>      ThreadFactory factory = Thread.builder().virtual().factory();
>      try (ExecutorService executor = Executors.newUnboundedExecutor(factory)) {
>          executor.submit(task1);
>          executor.submit(task2);
>      }
>
>  The new Executors.newUnboundedExecutor method creates an ExecutorService that
>  spawns a new thread for each submitted task -- in this case, a virtual thread
>  constructed by the provided factory.
>
>  [...]
>
> [1]: https://mail.openjdk.java.net/pipermail/loom-dev/2019-October/000825.html
> [2]: https://mail.openjdk.java.net/pipermail/loom-dev/2019-October/000825.html
> [3]: https://mail.openjdk.java.net/pipermail/loom-dev/2019-November/000864.html
> [4]: https://vorpus.org/blog/notes-on-structured-concurrency-or-go-statement-considered-harmful/

This talk focuses on the usage of Loom.
Therefore, the sections on "Performance", "Debugging", "Profiling and Monitoring", "Continuations", and "Stability" are left out on purpose.

Enough of theory and documentation.
Let's "copy and paste" some of the code snippets presented in order to get a JUnit Platform TestEngine implementation running on Loom.

## TestEngine: `junit5-looming`

https://github.com/sormuras/junit5-looming

JUnit Platform + "A Virtual Thread is a Thread" = junit5-looming
Starting with a simplistic "test" that sleeps some milliseconds:

		Thread.sleep((long) (Math.random() * 1000))

Generate a bunch of those "tests" and execute them all at once.
Make the amount of generated test configurable.
Raise the bar and see what happens.

		if (virtual) {
			var factory = Thread.builder().virtual().factory();
			return Executors.newUnboundedExecutor(factory);
		}
		return Executors.newFixedThreadPool(1000);

TODO: Live Demo

Round up with table of not-benchmarks:

| Processor              | Threads 10.000 | Threads 100.000 | Virtual 10.000 | Virtual 100.000 | Virtual 1.000.000|
|----------------------- |----------------|-----------------|----------------|---------------- |------------------|
| Ryzen 3700X (Win 10)   | 5.6            | 51              | 1.1            | 1.2             | 3.9 :rocket:     |
| i7-3770K (Win 10)      | 6              | 51              | 1.2            | 1.5             | 5.2 :rocket:     |
| i7-7920HQ (Mac OS)     | 6              | 51              | 1.2            | 1.7             | 7.1 :rocket:     |
| GitHub/Azure (Linux)   | 6              | n.a.            | 3              | 20              | 223              |
| [GH 2019-12-19] (Linux)| 7              | 53              | 3              | 18              | 227              |
| [GH 2020-01-25] (Linux)| 6              | 53              | 2              | 14              | 162              |
| [GH 2020-01-28] (Linux)| 7              | 53              | 3              | 16              | 150              |
| [GH 2020-02-27] (Linux)| 7              | 52              | 3              | 17              | 150              |

## TestEngine: `Jupiter` + Loom

https://github.com/junit-team/junit5/tree/loom

Jupiter utilizes Fork-Join-Pool for parallel test execution
Replace system threads with virtual threads in Jupiter - what else?
Initial prototype of parallel execution using virtual threads looked promising:
https://github.com/junit-team/junit5/commit/dfb8ab64b073d3e26af56e86038edea1b924727b#diff-37325a23fcbc3b777c6e974e8761f85c

		ThreadFactory threadFactory = Thread.builder().virtual().name("junit-executor", 1).factory();
		executorService = Executors.newUnboundedExecutor(threadFactory);

Execute a single test task:

		CompletableFuture.runAsync(testTask::execute, executorService);

Execute multiple test tasks:

		CompletableFuture.allOf(testTasks.stream().map(this::submit).toArray(CompletableFuture<?>[]::new)).join();

Too easy and we got more "green lights" than expected.

Finished the spike some hours later:
https://github.com/junit-team/junit5/commit/119345734f18792fe8831f9e5feaa0f70242e8e7#diff-37325a23fcbc3b777c6e974e8761f85c

After that, all parallel execution related tests went "green".

TODO: Create "big test suite" using Jupiter's normal, dynamic, and parameterized test features.
TODO: Explain and demo JitPack-ed distribution of the junit5/loom branch.

## Conclusion Project Loom

OpenJDK Early Access builds are fun to play with
Feedback is welcome on the related mailing list
For Project Loom that is: https://mail.openjdk.java.net/pipermail/loom-dev

## Conclusion JUnit 5 + Loom

Modular design of JUnit 5 works out
MR-JAR technology enables virtual threads on JDK 15-loom by default
Blocking tests (IO-intensive) will profit out of the box
Seeing 100.000+ (sleepy fake) tests executed in IDE is ... WOAH! (=
"Will you backport Loom support to JUnit 4?!"
