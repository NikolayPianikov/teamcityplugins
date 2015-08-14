package jetbrains.buildServer.dotTrace.agent;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.dotNet.buildRunner.agent.CommandLineSetup;
import jetbrains.buildServer.dotNet.buildRunner.agent.CommandLineSetupBuilder;
import jetbrains.buildServer.dotNet.buildRunner.agent.RunnerAssertions;
import jetbrains.buildServer.dotNet.buildRunner.agent.RunnerParametersService;
import jetbrains.buildServer.dotTrace.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class DotTraceSetupBuilder implements CommandLineSetupBuilder {
  private final RunnerParametersService myParametersService;
  private final RunnerAssertions myAssertions;

  public DotTraceSetupBuilder(
          @NotNull final RunnerParametersService parametersService,
          @NotNull final RunnerAssertions assertions) {
    myParametersService = parametersService;
    myAssertions = assertions;
  }

  @NotNull
  public Iterable<CommandLineSetup> build(@NotNull final CommandLineSetup baseSetup) {
    if(myAssertions.contains(RunnerAssertions.Assertion.PROFILING_IS_NOT_ALLOWED)) {
      return Collections.singleton(baseSetup);
    }

    String dotMemoryUnitTool = myParametersService.tryGetRunnerParameter(Constants.USE_VAR);
    if (StringUtil.isEmptyOrSpaces(dotMemoryUnitTool) || !Boolean.parseBoolean(dotMemoryUnitTool)) {
      return Collections.singleton(baseSetup);
    }

    String dotMemoryUnitPath = myParametersService.tryGetRunnerParameter(Constants.PATH_VAR);
    if(dotMemoryUnitPath == null) {
      dotMemoryUnitPath = "";
    }

    return Collections.singleton(baseSetup);
  }
}