<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="board" type="com.hcs.soundboard.data.Board"--%>

<hcs:standard-page title="Edit Soundboard" page="create">
    <div class="jumbotron">
        <h1><c:out value="${board.boardName}"/></h1>
        <p class="lead"><c:out value="${board.description}"/></p>
    </div>
    <div id="buttons">
        <c:forEach var="sound" items="${board.sounds}">
            <hcs:sound-button sound="${sound}" />
        </c:forEach>
    </div>
    <div>
        <hcs:form method="post" enctype="multipart/form-data" action="/board/${board.id}/upload">
            <input class="btn" type="file" accept="audio/*" capture="microphone" name="sound" required="required" />
            <input type="text" name="name" title="Name" required="required"/>
            <input class="btn" type="submit"/>
        </hcs:form>
    </div>
</hcs:standard-page>