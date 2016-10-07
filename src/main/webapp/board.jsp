<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="board" type="com.hcs.soundboard.data.Board"--%>

<hcs:standard-page title="Sounds" page="sounds">
    <div class="jumbotron">
        <h1><c:out value="${board.title}"/></h1>
        <p class="lead"><c:out value="${board.description}"/></p>
    </div>
    <div id="buttons">
        <c:forEach var="sound" items="${board.sounds}">
            <hcs:sound-button sound="${sound}" />
        </c:forEach>
    </div>
</hcs:standard-page>