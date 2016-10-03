<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes.jsp" %>

<hcs:standard-page title="Sounds" page="sounds">
    <div id="buttons">
        <c:forEach var="sound" items="${sounds}">
            <button id="${sound.id}" type="button" class="btn" onclick="new Audio('/sound/${sound.id}').play()">
                <c:out value="${sound.name}" />
            </button>
            <audio id="sound${sound.id}" preload="auto" src="/sound/${sound.id}"></audio>
        </c:forEach>
    </div>
</hcs:standard-page>