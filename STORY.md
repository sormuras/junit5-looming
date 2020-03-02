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
"Virtual Thread is a Thread"

## TestEngine: `junit5-looming`

JUnit Platform + "A Virtual Thread is a Thread" = junit5-looming
Starting with a simplistic "test" that sleeps some milliseconds:

		Thread.sleep((long) (Math.random() * 1000))

Generate a bunch of those "tests" and execute them all at once.
Make the amount of generated test configurable.
Raise the bar.

## TestEngine: `Jupiter` + Loom

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
