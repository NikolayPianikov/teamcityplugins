package jetbrains.buildServer.dotMemoryUnit.server;

import org.jetbrains.annotations.NotNull;

public class DotMemoryUnitBean {
  private static final String NUNIT_USE_DOT_MEMORY_UNIT = "nunit_use_dot_memory_unit";
  private static final String NUNIT_DOT_MEMORY_UNIT_PATH = "nunit_dot_memory_unit_path";
  public static final DotMemoryUnitBean Shared = new DotMemoryUnitBean();

  @NotNull
  public String getUseDotMemoryUnitKey() {
    return NUNIT_USE_DOT_MEMORY_UNIT;
  }

  @NotNull
  public String getDotMemoryUnitPathKey() {
    return NUNIT_DOT_MEMORY_UNIT_PATH;
  }
}
