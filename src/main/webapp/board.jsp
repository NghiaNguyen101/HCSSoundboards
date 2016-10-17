<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="board" type="com.hcs.soundboard.data.Board"--%>
<%--@elvariable id="useShared" type="java.lang.Boolean"--%>
<c:set var="version" value="${useShared ? board.sharedVersion : board.unsharedVersion}" />

<hcs:standard-page title="${version.title}" page="board">
    <div class="jumbotron">
        <h1><c:out value="${version.title}"/></h1>
        <p class="lead"><c:out value="${version.description}"/></p>
        <p class="lead">By <hcs:user username="${board.ownerName}"/></p>
    </div>
    <div id="buttons">
        <c:forEach var="sound" items="${version.sounds}">
            <hcs:sound-button sound="${sound}" />
        </c:forEach>
    </div>
</hcs:standard-page>