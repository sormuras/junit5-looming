import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Run {
  public static void main(String... args) throws Exception {
    start(false, 10_000);
    start(false, 100_000);
    start(true, 10_000);
    start(true, 100_000);
    start(true, 1_000_000);
  }

  static void start(boolean virtual, int tests) throws Exception {
    var java =
        new Command("java")
            .add(ProcessHandle.current().info().command().orElse("java"))
            .add("-XX:+UseContinuationChunks")
            .add("--module-path", List.of(Path.of("bin/main/modules"), Path.of("lib")))
            .add("--module", "org.junit.platform.console")
            .add("--details=SUMMARY")
            .add("--scan-modules")
            .add("--config", "virtual=" + virtual)
            .add("--config", "tests=" + tests);
    start(java.toStrings());
  }

  static void start(String... command) throws Exception {
    var line = String.join(" ", command);
    System.out.println("+ " + line);
    var process = new ProcessBuilder(command).inheritIO().start();
    if (process.waitFor() != 0) {
      throw new Error("Non-zero exit code for " + line);
    }
  }

  static class Command implements Cloneable {
    final String name;
    final List<String> arguments;

    Command(String name, String... arguments) {
      this.name = name;
      this.arguments = new ArrayList<>(List.of(arguments));
    }

    Command add(Object argument) {
      arguments.add(argument.toString());
      return this;
    }

    Command add(String key, Object argument) {
      return add(key).add(argument);
    }

    Command add(String key, List<Path> paths) {
      var strings = paths.stream().map(Path::toString);
      return add(key).add(strings.collect(Collectors.joining(File.pathSeparator)));
    }

    String[] toStrings() {
      return arguments.toArray(String[]::new);
    }
  }
}
