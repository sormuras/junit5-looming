open /*test*/ module test.integration {
  // module under test
  requires de.sormuras.junit.looming;
  // module we're testing with
  requires test.base;
  requires org.junit.platform.engine;
  uses org.junit.platform.engine.TestEngine;
}
