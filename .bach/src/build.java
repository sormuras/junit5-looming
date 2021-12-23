import com.github.sormuras.bach.Bach;
import com.github.sormuras.bach.external.JUnit;
import com.github.sormuras.bach.simple.SimpleSpace;

class build {
  public static void main(String... args) {
    try (var bach = new Bach(args)) {
      var grabber = bach.grabber(JUnit.version("5.8.1"));
      grabber.grabExternalModules("org.junit.jupiter", "org.junit.platform.console");
      grabber.grabMissingExternalModules();

      var builder = SimpleSpace.of(bach).withModule("com.github.sormuras.junit.looming");
      builder.compile(javac -> javac.add("--release", 19).add("--enable-preview"));
      builder.runJUnit("com.github.sormuras.junit.looming");
    }
  }
}
