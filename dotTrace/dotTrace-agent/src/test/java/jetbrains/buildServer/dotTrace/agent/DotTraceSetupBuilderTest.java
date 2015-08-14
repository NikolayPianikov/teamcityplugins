package jetbrains.buildServer.dotTrace.agent;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import jetbrains.buildServer.dotTrace.Constants;
import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import org.jetbrains.annotations.NotNull;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class DotTraceSetupBuilderTest {
  private Mockery myCtx;
  private RunnerParametersService myRunnerParametersService;
  private CommandLineResource myCommandLineResource;
  private RunnerAssertions myAssertions;

  @BeforeMethod
  public void setUp()
  {
    myCtx = new Mockery();
    //noinspection unchecked
    myRunnerParametersService = myCtx.mock(RunnerParametersService.class);
    myCommandLineResource = myCtx.mock(CommandLineResource.class);
    myAssertions = myCtx.mock(RunnerAssertions.class);
  }

  @Test
  public void shouldCreateSetupWhenGetSetup()
  {
    // Given
    final CommandLineSetup baseSetup = new CommandLineSetup("someTool", Arrays.asList(new CommandLineArgument("/arg1", CommandLineArgument.Type.PARAMETER), new CommandLineArgument("/arg2", CommandLineArgument.Type.PARAMETER)), Collections.singletonList(myCommandLineResource));
    final File projectFile = new File("aaa");
    final File outputFile = new File("output");
    final File workspaceDir = new File("workspaceDir");
    final File dotTraceFile = new File("c:\\abc", "aaa");
    myCtx.checking(new Expectations() {{
      oneOf(myAssertions).contains(RunnerAssertions.Assertion.PROFILING_IS_NOT_ALLOWED);
      will(returnValue(false));
      
      oneOf(myRunnerParametersService).tryGetRunnerParameter(Constants.PATH_VAR);
      will(returnValue(dotTraceFile.getParent()));

      oneOf(myRunnerParametersService).tryGetRunnerParameter(Constants.USE_VAR);
      will(returnValue("True"));
    }});

    final DotTraceSetupBuilder instance = createInstance();

    // When
    final CommandLineSetup setup = instance.build(baseSetup).iterator().next();

    // Then
    myCtx.assertIsSatisfied();
    // then(setup.getToolPath()).isEqualTo(dotTraceFile.getPath());
    // then(setup.getArgs()).containsExactly(new CommandLineArgument(projectFile.getPath(), CommandLineArgument.Type.PARAMETER));
  }

  @DataProvider(name = "runnerParamUseDotTraceCases")
  public Object[][] getParseTestsFromStringCases() {
    return new Object[][] {
      { null },
      { "" },
      { "abc" },
      { "False" },
      { "false" },
    };
  }

  @Test(dataProvider = "runnerParamUseDotTraceCases")
  public void shouldReturnBaseSetupWhenRunnerParamUseDotTraceIsEmptyOrFalse(final String useDotTrace)
  {
    // Given
    final CommandLineSetup baseSetup = new CommandLineSetup("someTool", Arrays.asList(new CommandLineArgument("/arg1", CommandLineArgument.Type.PARAMETER), new CommandLineArgument("/arg2", CommandLineArgument.Type.PARAMETER)), Collections.singletonList(myCommandLineResource));
    myCtx.checking(new Expectations() {{
      oneOf(myAssertions).contains(RunnerAssertions.Assertion.PROFILING_IS_NOT_ALLOWED);
      will(returnValue(false));

      oneOf(myRunnerParametersService).tryGetRunnerParameter(Constants.USE_VAR);
      will(returnValue(useDotTrace));
    }});

    final DotTraceSetupBuilder instance = createInstance();

    // When
    final CommandLineSetup setup = instance.build(baseSetup).iterator().next();

    // Then
    myCtx.assertIsSatisfied();
    then(setup).isEqualTo(baseSetup);
  }

  @Test
  public void shouldReturnBaseSetupWhenProfilingIsNotAllowed()
  {
    // Given
    final CommandLineSetup baseSetup = new CommandLineSetup("someTool", Arrays.asList(new CommandLineArgument("/arg1", CommandLineArgument.Type.PARAMETER), new CommandLineArgument("/arg2", CommandLineArgument.Type.PARAMETER)), Collections.singletonList(myCommandLineResource));
    myCtx.checking(new Expectations() {{
      oneOf(myAssertions).contains(RunnerAssertions.Assertion.PROFILING_IS_NOT_ALLOWED);
      will(returnValue(true));
    }});

    final DotTraceSetupBuilder instance = createInstance();

    // When
    final CommandLineSetup setup = instance.build(baseSetup).iterator().next();

    // Then
    myCtx.assertIsSatisfied();
    then(setup).isEqualTo(baseSetup);
  }

  @NotNull
  private DotTraceSetupBuilder createInstance()
  {
    return new DotTraceSetupBuilder(
      myRunnerParametersService,
      myAssertions);
  }
}
