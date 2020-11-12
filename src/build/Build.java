// default package

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.spi.ToolProvider;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Build {

  public static void main(String... args) throws Exception {
    System.out.println("Build");
    var build = new Build();
    var lib = build.assemble(Path.of("lib"));
    var main =
        build.compile(
            "main",
            List.of(lib),
            List.of("src/*/main/java"),
            List.of("de.sormuras.junit.looming"),
            "",
            List.of());
    var test =
        build.compile(
            "test",
            List.of(main, lib),
            List.of("src/*/test/java", "src/*/test/module"),
            List.of("test.base", "test.integration"),
            "src/*/main/java",
            List.of("de.sormuras.junit.looming"));

    build.test(
        test, List.of(main, test, lib), List.of("de.sormuras.junit.looming", "test.integration"));
  }

  final HttpClient http = HttpClient.newHttpClient();

  Path assemble(Path lib) throws Exception {
    Files.createDirectories(lib);
    load(lib, "org.apiguardian", "apiguardian-api", "1.1.0");
    load(lib, "org.opentest4j", "opentest4j", "1.2.0");
    var platform = "1.7.0";
    load(lib, "org.junit.platform", "junit-platform-commons", platform);
    load(lib, "org.junit.platform", "junit-platform-console", platform);
    load(lib, "org.junit.platform", "junit-platform-engine", platform);
    load(lib, "org.junit.platform", "junit-platform-launcher", platform);
    load(lib, "org.junit.platform", "junit-platform-reporting", platform);
    var jupiter = "5.7.0";
    load(lib, "org.junit.jupiter", "junit-jupiter", jupiter);
    load(lib, "org.junit.jupiter", "junit-jupiter-api", jupiter);
    load(lib, "org.junit.jupiter", "junit-jupiter-engine", jupiter);
    load(lib, "org.junit.jupiter", "junit-jupiter-params", jupiter);
    return lib;
  }

  Path compile(
      String realm,
      List<Path> modulePaths,
      List<String> sourcePaths,
      List<String> modules,
      String patchPattern,
      List<String> patches)
      throws Exception {
    var bin = Path.of("bin").resolve(realm);
    var classes = bin.resolve("javac");
    var jars = bin.resolve("modules");
    var all = Stream.of(modules, patches).flatMap(List::stream).collect(Collectors.toList());

    var javac =
        new Command("javac")
            .add("-d", classes)
            .add("-Xlint:all")
            .add("--module-source-path", String.join(File.pathSeparator, sourcePaths))
            .add("--module-path", modulePaths)
            .add("--module", String.join(",", all));
    for (var patch : patches) {
      javac.add("--patch-module", patch + "=" + patchPattern.replace("*", patch));
    }
    run(javac.name, javac.toStrings());

    Files.createDirectories(jars);
    for (var module : all) {
      var jar =
          new Command("jar")
              .add("--create")
              .add("--file", jars.resolve(module + ".jar"))
              .add("-C", classes.resolve(module))
              .add(".");
      run(jar.name, jar.toStrings());
    }
    return jars;
  }

  void test(Path test, List<Path> modulePaths, List<String> modules) throws Exception {
    for (var module : modules) {
      var paths = new ArrayList<>(List.of(test.resolve(module + ".jar")));
      paths.addAll(modulePaths);
      test(paths, module);
    }
  }

  void test(List<Path> modulePaths, String module) throws Exception {
    var junit =
        new Command("junit")
            .add(ProcessHandle.current().info().command().orElse("java"))
            .add("--module-path", modulePaths)
            .add("--add-modules", module)
            .add("--module", "org.junit.platform.console")
            .add("--select-module", module);
    start(junit.toStrings());
  }

  void load(Path lib, String group, String artifact, String version) throws Exception {
    var repository = "https://repo1.maven.org/maven2";
    var file = artifact + '-' + version + ".jar";
    var source = String.join("/", repository, group.replace('.', '/'), artifact, version, file);
    var target = Files.createDirectories(lib).resolve(file);
    if (Files.exists(target)) {
      return;
    }
    var request = HttpRequest.newBuilder(URI.create(source)).GET().build();
    var response = http.send(request, HttpResponse.BodyHandlers.ofFile(target));
    if (response.statusCode() == 200) {
      return;
    }
    throw new Error("Non-200 status code: " + response);
  }

  void run(String name, String... args) {
    var strings = args.length == 0 ? "" : '"' + String.join("\", \"", args) + '"';
    System.out.printf("| %s(%s)%n", name, strings);
    var tool = ToolProvider.findFirst(name).orElseThrow();
    int code = tool.run(System.out, System.err, args);
    if (code != 0) {
      throw new RuntimeException("Non-zero exit code: " + code);
    }
  }

  void start(String... command) throws Exception {
    var line = String.join(" ", command);
    System.out.println("+ " + line);
    var process = new ProcessBuilder(command).inheritIO().start();
    if (process.waitFor() != 0) {
      throw new Error("Non-zero exit code for " + line);
    }
  }

  static class Command {
    final String name;
    final List<String> arguments;

    Command(String name) {
      this.name = name;
      this.arguments = new ArrayList<>();
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
