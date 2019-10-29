module de.sormuras.junit.looming {
  requires org.junit.platform.engine;

  provides org.junit.platform.engine.TestEngine with
      de.sormuras.junit.looming.LoomTestEngine;
}
