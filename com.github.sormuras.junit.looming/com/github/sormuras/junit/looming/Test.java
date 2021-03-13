package com.github.sormuras.junit.looming;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

class Test extends AbstractTestDescriptor implements Runnable {

  Test(UniqueId uniqueId, int index) {
    super(uniqueId.append("test", "#" + index), "LoomingTest #" + index);
  }

  @Override
  public Type getType() {
    return Type.TEST;
  }

  @Override
  public void run() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    } catch (InterruptedException e) {
      // ignore
    }
  }
}
