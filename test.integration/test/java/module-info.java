open /*test*/ module test.integration {
  // module under test
  requires com.github.sormuras.junit.looming;
  // modules we're testing with
  requires org.junit.jupiter;
  requires org.junit.platform.console;
  requires org.junit.platform.engine;

  uses org.junit.platform.engine.TestEngine;
}
