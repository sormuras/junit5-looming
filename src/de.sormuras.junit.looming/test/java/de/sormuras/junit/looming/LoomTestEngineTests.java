package de.sormuras.junit.looming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoomTestEngineTests {

  @Test
  void checkId() {
    assertEquals("looming", new LoomTestEngine().getId());
  }
}
