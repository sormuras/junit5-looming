import com.github.sormuras.bach.Bach;
import com.github.sormuras.bach.Configuration;
import com.github.sormuras.bach.ToolCall;
import com.github.sormuras.bach.ToolFinder;
import com.github.sormuras.bach.ToolRunner;
import com.github.sormuras.bach.project.Project;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;

class build {
  public static void main(String... args) {
    var bach =
        new Bach(
            Configuration.ofDefaults(),
            Project.ofDefaults()
                .withExternalModules("junit", "5.8.2")
                .withRequiresModule("org.junit.jupiter")
                .withRequiresModule("org.junit.platform.console"));

    bach.run("cache"); // go offline by caching all required external assets that are missing

    bach.run(
        ToolCall.of("javac")
            .with("--release", 19)
            .with("--enable-preview")
            .with("--module", "com.github.sormuras.junit.looming")
            .with("--module-source-path", ".")
            .with("--module-path", ".bach/external-modules")
            .with("-d", ".bach/out/main/classes/java-19-preview"));

    bach.run(
        ToolFinder.of(
            ModuleFinder.of(
                Path.of(".bach/out/main/classes/java-19-preview"),
                Path.of(".bach/external-modules")),
            true,
            "com.github.sormuras.junit.looming"),
        ToolCall.of("junit").with("--scan-modules"),
        ToolRunner.RunModifier.RUN_WITH_PROVIDERS_CLASS_LOADER);
  }
}
