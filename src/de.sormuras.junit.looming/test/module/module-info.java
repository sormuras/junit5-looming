open /*test*/ module de.sormuras.junit.looming /*extends main module descriptor*/ {
  // << Copied from "src/*/main/java/module-info.java"...
  requires org.junit.platform.engine;
  provides org.junit.platform.engine.TestEngine with
      de.sormuras.junit.looming.LoomTestEngine;
  // >>

  // requires org.junit.jupiter;
  requires test.base;
}
