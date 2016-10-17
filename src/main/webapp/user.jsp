<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="username" type="String"--%>
<%--@elvariable id="boards" type="java.util.List<com.hcs.soundboard.data.Board>"--%>

<hcs:standard-page title="Browse" page="browse">
    <div>
        <h1><c:out value="${username}"/></h1>
    </div>
    <c:forEach var="board" items="${boards}">
        <div>
            <p><a href="/board/${board.id}"><b><c:out value="${board.sharedVersion.title}"/></b></a></p>
            <p><c:out value="${board.sharedVersion.description}"/></p>
            <hr/>
        </div>
    </c:forEach>
</hcs:standard-page>