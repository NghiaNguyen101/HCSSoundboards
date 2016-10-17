<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="boards" type="java.util.List<com.hcs.soundboard.data.Board>"--%>

<hcs:standard-page title="Browse" page="browse">
    <c:forEach var="board" items="${boards}">
        <div>
            <p><a href="/board/${board.id}"><b><c:out value="${board.sharedVersion.title}"/></b></a></p>
            <p><c:out value="${board.sharedVersion.description}"/></p>
            <p>By <hcs:user username="${board.ownerName}"/></p>
            <hr/>
        </div>
    </c:forEach>
</hcs:standard-page>