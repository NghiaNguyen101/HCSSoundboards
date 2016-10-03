<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag dynamic-attributes="dynattrs" %>

<form
<c:forEach items="${dynattrs}" var="a">
    ${a.key}="${a.value}"
</c:forEach>
>
    <jsp:doBody />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>