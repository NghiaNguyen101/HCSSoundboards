<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<hcs:standard-page title="Report" page="all-report">
    <!-- Select report status resolved or not -->
    <hcs:form action="/all-report">
        <div class="btn-group btn-toggle">
            <button class="btn ${status == false ? "btn-success" : "btn-default"}" type="submit"
                    name="resolved" value="0">Unresolved</button>
            <button class="btn ${status == true ? "btn-success" : "btn-default"}" type="submit"
                    name="resolved" value="1">Resolved</button>
        </div>
    </hcs:form>
    <br />
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
