<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="bean" class="jetbrains.buildServer.dotMemoryUnit.server.DotMemoryUnitBean"/>

<div class="parameter">
  Use dotMemory Unit: <props:displayValue name="${bean.useDotMemoryUnitKey}" emptyValue="false"/>
</div>

<div class="parameter">
  Path to dotMemory Unit: <props:displayValue name="${bean.dotMemoryUnitPathKey}" emptyValue="<empty>"/>
</div>
