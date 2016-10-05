<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Your Boards" page="your-boards">
    <%--@elvariable id="boards" type="java.util.List<com.hcs.soundboard.data.Board>"--%>
    <c:forEach var="board" items="${boards}">
        <div>
            <p><a href="/board/${board.id}"><b><c:out value="${board.boardName}"/></b></a></p>
            <p><c:out value="${board.description}"/></p>
            <p>${board.isPublic() ? 'Public' : 'Private'}</p>
            <p><a href="/board/${board.id}/edit">Edit</a></p>
            <hr />
        </div>
    </c:forEach>
</hcs:standard-page>