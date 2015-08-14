package jetbrains.buildServer.dotMemoryUnit.server;

import jetbrains.buildServer.dotMemoryUnit.Constants;
import org.jetbrains.annotations.NotNull;

public class DotMemoryUnitBean {
  public static final DotMemoryUnitBean Shared = new DotMemoryUnitBean();

  @NotNull
  public String getUseDotMemoryUnitKey() {
    return Constants.NUNIT_USE_DOT_MEMORY_UNIT;
  }

  @NotNull
  public String getDotMemoryUnitPathKey() {
    return Constants.NUNIT_DOT_MEMORY_UNIT_PATH;
  }
}
