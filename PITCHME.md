# JUnit 5 & Project Loom

---

## JUnit Platform

- `TestEngine`

---

## Project Loom

- Threads, Fibers, Conties...

+++

- "A lightweight thread is a Thread"
- https://mail.openjdk.java.net/pipermail/loom-dev/2019-October/000796.html

---

## `LoomTestEngine`

- A test is `Thread.sleep(1000)`
- https://github.com/sormuras/junit5-looming

---

## JUnit Jupiter

- Current Concurrent API
- https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution

+++

- Current Execution Model Impl: `ForkJoinPool`
- https://github.com/junit-team/junit5/blob/master/junit-platform-engine/src/main/java/org/junit/platform/engine/support/hierarchical/ForkJoinPoolHierarchicalTestExecutorService.java

+++

- Back a thread per request (read: test)

--

## Outlook

-- 

## Sponsors and Questions

