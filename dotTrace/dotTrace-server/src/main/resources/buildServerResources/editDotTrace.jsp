<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"  %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="bean" class="jetbrains.buildServer.dotTrace.server.DotTraceBean"/>

<tr class="advancedSetting">
  <th><label for="${bean.useDotTraceKey}">Use dotTrace:</label></th>
  <td><props:checkboxProperty name="${bean.useDotTraceKey}" />
    <span class="smallNote">The tests will be started under the JetBrains dotTrace tool.</span>
    <span class="error" id="error_${bean.useDotTraceKey}"></span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${bean.dotTracePathKey}">Path to dotTrace: <l:star/></label></th>
  <td>
    <div class="completionIconWrapper">
      <props:textProperty name="${bean.dotTracePathKey}" className="longField"/>
    </div>
    <span class="error" id="error_${bean.dotTracePathKey}"></span>
    <span class="smallNote">Specify path to dotTrace.exe</span>
  </td>
</tr>