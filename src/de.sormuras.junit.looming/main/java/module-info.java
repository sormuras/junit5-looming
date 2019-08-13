module de.sormuras.junit.looming {
  requires org.junit.platform.engine;
  requires org.junit.platform.console;

  provides org.junit.platform.engine.TestEngine with
      de.sormuras.junit.looming.LoomTestEngine;
}
