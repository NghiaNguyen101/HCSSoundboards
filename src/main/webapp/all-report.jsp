<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<hcs:standard-page title="Report" page="all-report">

    <%--@elvariable id="reports" type="java.util.List<com.hcs.soundboard.data.Report>"--%>
    <c:forEach var="report" items="${reports}" varStatus="loop">
        <div>
            <c:if test="${loop.index != 0}">
                <hr />
            </c:if>
            <p><a href="/report/${report.reportId}"><b>Report #<c:out value="${report.reportId}"/></b></a></p>
            <p><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${report.reportDate}"/></p>
            <p><c:out value="${report.reportDesc}"/></p>
            <p>By <hcs:user username="${report.reportUser}"/></p>
        </div>
    </c:forEach>
</hcs:standard-page>
