<%@ attribute name="items" type="java.util.List" required="true" %>
<%@ attribute name="var" type="java.lang.String" required="true" %>

<%@ include file="/includes.jsp" %>

<c:forEach items="${items}" var="#{var}">
    <c:if test="${loop.index != 0}">
        <hr />
    </c:if>
    <jsp:doBody/>
</c:forEach>