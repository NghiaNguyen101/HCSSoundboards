<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<hcs:standard-page title="Report ${report.reportId}" page="board">
    <div class="jumbotron">
        <h1><c:out value="${report.reportTitle}"/></h1>
        <p class="lead">Date: <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${report.reportDate}"/></p>
        <p class="lead">By <hcs:user username="${report.reportUser}"/></p>
        <p class="lead">For <a href="/board/${report.boardId}">Board</a>
            created by <hcs:user username="${report.boardOwner}"/></p>
    </div>
    <div>
        <p><c:out value="${report.reportDesc}"/></p>
    </div>

    <hcs:form action="/report/${report.reportId}/resolved" method="post" id="resolved_report">
        <input class="btn btn-warning" type="submit" value="Resolved" />
    </hcs:form>

</hcs:standard-page>
