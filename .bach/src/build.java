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
                .withModule("main", "com.github.sormuras.junit.looming")
                .withTargetsJava("main", Runtime.version().feature())
                .withAdditionalCompileJavacArguments("main", "--enable-preview")
                .withExternalModules("junit", "5.8.2")
                .withRequiresModule("org.junit.jupiter")
                .withRequiresModule("org.junit.platform.console"));

    bach.run("cache"); // go offline by caching all required external assets that are missing
    bach.run("compile"); // compile all modules in all project spaces
    bach.run(
        ToolFinder.of(
            ModuleFinder.of(
                Path.of(".bach/out/main/modules"),
                Path.of(".bach/external-modules")),
            true,
            "com.github.sormuras.junit.looming"),
        ToolCall.of("junit").with("--scan-modules"),
        ToolRunner.RunModifier.RUN_WITH_PROVIDERS_CLASS_LOADER);
  }
}
