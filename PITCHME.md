# JUnit 5 & Project Loom

---

## JUnit Platform

- `TestEngine`

---

## Project Loom

- Threads, Fibers, Conties...

+++

- "A lightweight thread is a Thread"
- https://mail.openjdk.java.net/pipermail/loom-dev/2019-October/000796.html A lightweight thread is a Thread by Alan Bateman
- https://www.youtube.com/watch?v=lIq-x_iI-kc Project Loom: Helping Write Concurrent Applications on the Java Platform by Ron Pressler
---

## LoomTestEngine

- Every "test" calls `Thread.sleep(1000)`
- https://github.com/sormuras/junit5-looming

---

## JUnit Jupiter

- Current Concurrent API
- https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution

+++

- Current Execution Model Impl: `ForkJoinPool`
- https://github.com/junit-team/junit5/blob/master/junit-platform-engine/src/main/java/org/junit/platform/engine/support/hierarchical/ForkJoinPoolHierarchicalTestExecutorService.java

+++

- Create Thread per Jupiter Test

---

## Outlook

---

## Sponsors and Questions

