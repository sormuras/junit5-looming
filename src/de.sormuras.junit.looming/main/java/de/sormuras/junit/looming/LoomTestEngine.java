package de.sormuras.junit.looming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

/** Test engine implementation generating and executing "sleepy tests". */
public class LoomTestEngine implements TestEngine {

  /**
   * Number of tests to generate.
   *
   * <p>{@code -Dtests=12345}
   *
   * @see <a
   *     href="https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params">JUnit
   *     Configuration Parameters</a>
   */
  public static final int TESTS = 20;

  @Override
  public String getId() {
    return "looming";
  }

  @Override
  public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
    var virtual = request.getConfigurationParameters().getBoolean("virtual").orElse(false);
    var caption = "Looming on " + Runtime.version() + " [" + (virtual ? "virtual" : "system") + "]";
    var engine = new EngineDescriptor(uniqueId, caption);
    int tests = request.getConfigurationParameters().get("tests", Integer::parseInt).orElse(TESTS);
    System.out.println("Creating " + tests + " tests");
    for (int i = 0; i < tests; i++) {
      engine.addChild(new Test(engine.getUniqueId(), i));
    }
    return engine;
  }

  @Override
  public void execute(ExecutionRequest request) {
    var engine = request.getRootTestDescriptor();
    var listener = request.getEngineExecutionListener();
    var virtual = request.getConfigurationParameters().getBoolean("virtual").orElse(false);
    var tests = engine.getChildren();
    System.out.println("Scheduling " + tests.size() + " tests");
    listener.executionStarted(engine);
    try (var executor = newExecutorService(virtual)) {
      for (var test : tests) {
        listener.executionStarted(test);
        var future = CompletableFuture.runAsync((Runnable) test, executor);
        future.whenCompleteAsync(markSuccessfullyFinished(listener, test));
      }
      System.out.printf("Awaiting all %s threads to complete...%n", virtual ? "virtual" : "system");
    } finally {
      listener.executionFinished(engine, TestExecutionResult.successful());
    }
  }

  private static ExecutorService newExecutorService(boolean virtual) {
    if (virtual) {
      var factory = Thread.builder().virtual().factory();
      return Executors.newUnboundedExecutor(factory);
    }
    return Executors.newFixedThreadPool(1000);
  }

  private static BiConsumer<Object, Throwable> markSuccessfullyFinished(
      EngineExecutionListener listener, TestDescriptor test) {

    return (r, t) -> listener.executionFinished(test, TestExecutionResult.successful());
  }
}
