package jetbrains.buildServer.dotTrace.agent;

import com.intellij.openapi.util.text.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import jetbrains.buildServer.dotTrace.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class DotTraceSetupBuilder implements CommandLineSetupBuilder {
  static final String DOT_TRACE_EXE_NAME = "ConsoleProfiler.exe";
  static final String DOT_TRACE_PROJECT_EXT = ".dotTrace";

  private final ResourceGenerator<DotTraceContext> myDotTraceProjectGenerator;
  private final ResourcePublisher myBeforeBuildPublisher;
  private final RunnerParametersService myParametersService;
  private final FileService myFileService;
  private final RunnerAssertions myAssertions;

  public DotTraceSetupBuilder(
    @NotNull final ResourceGenerator<DotTraceContext> dotTraceProjectGenerator,
    @NotNull final ResourcePublisher beforeBuildPublisher,
    @NotNull final RunnerParametersService parametersService,
    @NotNull final FileService fileService,
    @NotNull final RunnerAssertions assertions) {
    myDotTraceProjectGenerator = dotTraceProjectGenerator;
    myBeforeBuildPublisher = beforeBuildPublisher;
    myParametersService = parametersService;
    myFileService = fileService;
    myAssertions = assertions;
  }

  @NotNull
  public Iterable<CommandLineSetup> build(@NotNull final CommandLineSetup baseSetup) {
    if(myAssertions.contains(RunnerAssertions.Assertion.PROFILING_IS_NOT_ALLOWED)) {
      return Collections.singleton(baseSetup);
    }

    String dotTraceTool = myParametersService.tryGetRunnerParameter(Constants.USE_VAR);
    if (StringUtil.isEmptyOrSpaces(dotTraceTool) || !Boolean.parseBoolean(dotTraceTool)) {
      return Collections.singleton(baseSetup);
    }

    String dotTracePath = myParametersService.tryGetRunnerParameter(Constants.PATH_VAR);
    if(dotTracePath == null) {
      dotTracePath = "";
    }

    String dotTraceThresholds = myParametersService.tryGetRunnerParameter(Constants.THRESHOLDS_VAR);
    if(dotTraceThresholds == null) {
      dotTraceThresholds = "";
    }

    File toolPath = new File(dotTracePath, DOT_TRACE_EXE_NAME);
    myFileService.validatePath(toolPath);

    List<CommandLineResource> resources = new ArrayList<CommandLineResource>(baseSetup.getResources());
    final File projectFile = myFileService.getTempFileName(DOT_TRACE_PROJECT_EXT);
    final String projectFileContent = myDotTraceProjectGenerator.create(new DotTraceContext(baseSetup));
    resources.add(new CommandLineFile(myBeforeBuildPublisher, projectFile, projectFileContent));

    return Collections.singleton(new CommandLineSetup(toolPath.getPath(), Collections.singletonList(new CommandLineArgument(projectFile.getPath(), CommandLineArgument.Type.PARAMETER)), resources));
  }
}