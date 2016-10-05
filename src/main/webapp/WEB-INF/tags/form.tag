<%@ tag dynamic-attributes="dynattrs" %>

<%@ include file="/includes.jsp" %>

<form
<c:forEach items="${dynattrs}" var="a">
    ${a.key}="${a.value}"
</c:forEach>
>
    <jsp:doBody />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>