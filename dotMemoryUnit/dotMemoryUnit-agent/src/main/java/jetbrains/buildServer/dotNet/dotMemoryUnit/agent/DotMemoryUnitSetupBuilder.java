package jetbrains.buildServer.dotNet.dotMemoryUnit.agent;

import com.intellij.openapi.util.text.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import org.jetbrains.annotations.NotNull;

public class DotMemoryUnitSetupBuilder implements CommandLineSetupBuilder {
  static final String DOT_MEMORY_UNIT_EXE_NAME = "dotMemoryUnit.exe";
  static final String DOT_MEMORY_UNIT_PROJECT_EXT = ".dotMemoryUnit";
  static final String DOT_MEMORY_UNIT_OUTPUT_EXT = ".dotMemoryUnitResult";
  static final String NUNIT_USE_DOT_MEMORY_UNIT = "nunit_use_dot_memory_unit";
  static final String NUNIT_DOT_MEMORY_UNIT_PATH = "nunit_dot_memory_unit_path";

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

    String dotMemoryUnitTool = myParametersService.tryGetRunnerParameter(NUNIT_USE_DOT_MEMORY_UNIT);
    if (StringUtil.isEmptyOrSpaces(dotMemoryUnitTool) || !Boolean.parseBoolean(dotMemoryUnitTool)) {
      return Collections.singleton(baseSetup);
    }

    String dotMemoryUnitPath = myParametersService.tryGetRunnerParameter(NUNIT_DOT_MEMORY_UNIT_PATH);
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
    return Collections.singleton(new CommandLineSetup(toolPath.getPath(), Arrays.asList(new CommandLineArgument(projectFile.getPath(), CommandLineArgument.Type.PARAMETER)), resources));
  }
}