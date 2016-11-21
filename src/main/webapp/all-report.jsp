<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<hcs:standard-page title="Report" page="report">
    <c:forEach var="report" items="${reports}">
        <div>
            <p><a href="/report/${report.reportId}"><b><c:out value="${report.reportTitle}"/></b></a></p>
            <p><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${report.reportDate}"/></p>
            <p>By <hcs:user username="${report.reportUser}"/></p>
            <hr/>
        </div>
    </c:forEach>
</hcs:standard-page>
