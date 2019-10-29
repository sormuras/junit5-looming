package de.sormuras.junit.looming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

public class LoomTestEngine implements TestEngine {

  @Override
  public String getId() {
    return "looming";
  }

  @Override
  public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
    var caption = "Loom TestEngine on " + Runtime.version();
    var engine = new EngineDescriptor(uniqueId, caption);
    int tests = request.getConfigurationParameters().get("tests", Integer::parseInt).orElse(20_000);
    for (int i = 0; i < tests; i++) {
      engine.addChild(new Test(engine.getUniqueId(), i));
    }
    return engine;
  }

  @Override
  public void execute(ExecutionRequest request) {
    var engine = request.getRootTestDescriptor();
    var listener = request.getEngineExecutionListener();
    listener.executionStarted(engine);
    if (request.getConfigurationParameters().getBoolean("thread").orElse(false)) {
      executeInThreadPool(engine, listener);
    } else {
      executeInFiberScope(engine, listener);
    }
    listener.executionFinished(engine, TestExecutionResult.successful());
  }

  private void executeInThreadPool(TestDescriptor engine, EngineExecutionListener listener) {
    System.out.println("LoomTestEngine.executeInThreadPool");
    var executor = Executors.newFixedThreadPool(1000);
    System.out.println(executor);
    try {
      var tests = engine.getChildren();
      System.out.println("scheduling " + tests.size() + " tests via executor");
      for (var test : tests) {
        listener.executionStarted(test);
        var future = CompletableFuture.runAsync((Runnable) test, executor);
        future.whenCompleteAsync(markSuccessfullyFinished(listener, test));
      }
      System.out.println("awaiting all threads to complete...");
      executor.shutdown();
      executor.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      System.err.println("Shutdown termination NOT completed gracefully!");
    }
    System.out.println(executor);
  }

  private void executeInFiberScope(TestDescriptor engine, EngineExecutionListener listener) {
    System.out.println("LoomTestEngine.executeInFiberScope");
    try (var scope = FiberScope.open()) {
      System.out.println(scope);
      var tests = engine.getChildren();
      System.out.println("scheduling " + tests.size() + " tests via fiber scope");
      for (var test : tests) {
        listener.executionStarted(test);
        var future = scope.schedule((Runnable) test).toFuture();
        future.whenCompleteAsync(markSuccessfullyFinished(listener, test));
      }
      System.out.println("awaiting all fibers to complete...");
    }
  }

  private BiConsumer<Object, Throwable> markSuccessfullyFinished(
      EngineExecutionListener listener, TestDescriptor child) {
    return (r, t) -> listener.executionFinished(child, TestExecutionResult.successful());
  }
}
