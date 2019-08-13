package de.sormuras.junit.looming;

import java.io.PrintWriter;
import java.util.Map;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

class Main {
  public static void main(String... args) {
    var listener = new SummaryGeneratingListener();
    var launcherConfig =
        LauncherConfig.builder()
            .addTestEngines(new LoomTestEngine())
            .addTestExecutionListeners(listener)
            .build();
    var launcher = LauncherFactory.create(launcherConfig);
    var request =
        LauncherDiscoveryRequestBuilder.request()
            // .configurationParameters(Map.of("thread", "true", "tests", "" + 20_000))
            // .configurationParameters(Map.of("thread", "false", "tests", "" + 20_000))
            .configurationParameters(Map.of("thread", "false", "tests", "" + 1_000_000))
            .build();

    launcher.execute(request);

    var summary = listener.getSummary();
    var failures = summary.getTotalFailureCount();
    if (failures == 0) {
      summary.printTo(new PrintWriter(System.out, true));
      return;
    }
    summary.printFailuresTo(new PrintWriter(System.err, true));
    throw new AssertionError(failures + " failure(s)...");
  }
}
