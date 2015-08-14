package jetbrains.buildServer.dotMemoryUnit.agent;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.dotMemoryUnit.Constants;
import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DotMemoryUnitSetupBuilder implements CommandLineSetupBuilder {
  static final String DOT_MEMORY_UNIT_EXE_NAME = "dotMemoryUnit.exe";
  static final String DOT_MEMORY_UNIT_PROJECT_EXT = ".dotMemoryUnit";
  static final String DOT_MEMORY_UNIT_OUTPUT_EXT = ".dotMemoryUnitResult";

  private final ResourceGenerator<DotMemoryUnitContext> myDotMemoryUnitProjectGenerator;
  private final ResourcePublisher myBeforeBuildPublisher;
  private final ResourcePublisher myDotMemoryUnitPublisher;
  private final RunnerParametersService myParametersService;
  private final FileService myFileService;
  private final RunnerAssertions myAssertions;

  public DotMemoryUnitSetupBuilder(
    @NotNull final ResourceGenerator<DotMemoryUnitContext> dotMemoryUnitProjectGenerator,
    @NotNull final ResourcePublisher beforeBuildPublisher,
    @NotNull final ResourcePublisher dotMemoryUnitPublisher,
    @NotNull final RunnerParametersService parametersService,
    @NotNull final FileService fileService,
    @NotNull final RunnerAssertions assertions) {
    myDotMemoryUnitProjectGenerator = dotMemoryUnitProjectGenerator;
    myBeforeBuildPublisher = beforeBuildPublisher;
    myDotMemoryUnitPublisher = dotMemoryUnitPublisher;
    myParametersService = parametersService;
    myFileService = fileService;
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

    File toolPath = new File(dotMemoryUnitPath, DOT_MEMORY_UNIT_EXE_NAME);
    myFileService.validatePath(toolPath);

    List<CommandLineResource> resources = new ArrayList<CommandLineResource>(baseSetup.getResources());
    final File projectFile = myFileService.getTempFileName(DOT_MEMORY_UNIT_PROJECT_EXT);
    final File outputFile = myFileService.getTempFileName(DOT_MEMORY_UNIT_OUTPUT_EXT);
    final File workspaceDirectory = myFileService.getTempDirectory();
    final String projectFileContent = myDotMemoryUnitProjectGenerator.create(new DotMemoryUnitContext(baseSetup, workspaceDirectory, outputFile));
    resources.add(new CommandLineFile(myBeforeBuildPublisher, projectFile, projectFileContent));
    resources.add(new CommandLineArtifact(myDotMemoryUnitPublisher, outputFile));
    return Collections.singleton(new CommandLineSetup(toolPath.getPath(), Collections.singletonList(new CommandLineArgument(projectFile.getPath(), CommandLineArgument.Type.PARAMETER)), resources));
  }
}