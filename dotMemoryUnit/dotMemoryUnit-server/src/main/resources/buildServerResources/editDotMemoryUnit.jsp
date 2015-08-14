<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"  %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="bean" class="jetbrains.buildServer.dotMemoryUnit.server.DotMemoryUnitBean"/>

<tr class="advancedSetting">
  <th><label for="${bean.useDotMemoryUnitKey}">Use dotMemoryUnit:</label></th>
  <td><props:checkboxProperty name="${bean.useDotMemoryUnitKey}" />
    <span class="smallNote">The tests will be started under the JetBrains dotMemoryUnit tool.</span>
    <span class="error" id="error_${bean.useDotMemoryUnitKey}"></span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${bean.dotMemoryUnitPathKey}">Path to dotMemoryUnit: <l:star/></label></th>
  <td>
    <div class="completionIconWrapper">
      <props:textProperty name="${bean.dotMemoryUnitPathKey}" className="longField"/>
    </div>
    <span class="error" id="error_${bean.dotMemoryUnitPathKey}"></span>
    <span class="smallNote">Specify path to dotMemoryUnit.exe</span>
  </td>
</tr>