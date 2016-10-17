<%@ attribute name="username" %>
<%@ include file="/includes.jsp" %>
<a href="/user/${username}"><c:out value="${username}"/></a>