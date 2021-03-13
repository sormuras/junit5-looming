module com.github.sormuras.junit.looming {
  requires org.junit.platform.engine;
  provides org.junit.platform.engine.TestEngine with
      com.github.sormuras.junit.looming.LoomTestEngine;
}
