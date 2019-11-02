package de.sormuras.junit.looming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import test.base.PrependModuleName;

@DisplayNameGeneration(PrependModuleName.class)
class LoomTestEngineTests {

  @Test
  void checkId() {
    assertEquals("looming", new LoomTestEngine().getId());
  }
}
