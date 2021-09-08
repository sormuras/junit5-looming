import com.github.sormuras.bach.Bach;
import com.github.sormuras.bach.external.JUnit;

class build {
  public static void main(String... args) {
    try (var bach = new Bach(args)) {
      var grabber = bach.grabber(JUnit.version("5.8.0-M1"));
      grabber.grabExternalModules("org.junit.jupiter", "org.junit.platform.console");
      grabber.grabMissingExternalModules();

      var builder = bach.builder().conventional("com.github.sormuras.junit.looming");
      builder.compile();
      builder.runJUnit("com.github.sormuras.junit.looming");
    }
  }
}