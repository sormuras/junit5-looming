package test.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import test.base.PrependModuleName;

@DisplayNameGeneration(PrependModuleName.class)
class IntegrationTests {

  @Test
  void test() throws Exception {
    var module = Class.forName("de.sormuras.junit.looming.LoomTestEngine").getModule();
    assertEquals("de.sormuras.junit.looming", module.getName());
    assertEquals(Set.of(), module.getDescriptor().exports());
    assertEquals(
        "[org.junit.platform.engine.TestEngine with [de.sormuras.junit.looming.LoomTestEngine]]",
        module.getDescriptor().provides().toString());
  }
}
