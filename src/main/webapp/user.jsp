<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="username" type="String"--%>
<%--@elvariable id="boards" type="java.util.List<com.hcs.soundboard.data.Board>"--%>

<hcs:standard-page title="Browse" page="browse">
    <div>
        <h1><c:out value="${username}"/></h1>
    </div>
    <c:forEach var="board" items="${boards}" varStatus="loop">
        <div>
            <c:if test="${loop.index != 0}">
                <hr />
            </c:if>
            <p><a href="/board/${board.id}"><b><c:out value="${board.sharedVersion.title}"/></b></a></p>
            <p><c:out value="${board.sharedVersion.description}"/></p>
        </div>
    </c:forEach>
</hcs:standard-page>