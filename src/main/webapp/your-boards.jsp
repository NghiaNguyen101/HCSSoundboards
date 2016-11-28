<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Your Boards" page="your-boards">
    <%--@elvariable id="boards" type="java.util.List<com.hcs.soundboard.data.Board>"--%>
    <c:forEach var="board" items="${boards}">
        <div>
            <c:choose>
                <c:when test="${board.hasBeenShared()}">
                    <p style="font-size:24px"><a href="/board/${board.id}"><b><c:out value="${board.sharedVersion.title}"/></b></a></p>
                    <p style="font-size:18px"><b><c:out value="${board.sharedVersion.description}"/></b></p>
                    <p>Shared</p>
                    <p>${board.hasUnsharedChanges() ? 'Unshared Changes' : 'No Unshared Changes'}</p>
                    <p>${board.hidden ? 'Hidden' : 'Not Hidden'}</p>
                    <p><a href="/board/${board.id}/edit">Edit</a></p>
                </c:when>
                <c:otherwise>
                    <p style="font-size:24px"><a href="/board/${board.id}/edit"><b><c:out value="${board.unsharedVersion.title}"/></b></a></p>
                    <p style="font-size:18px"><b><c:out value="${board.unsharedVersion.description}"/></b></p>
                    <p>Not Yet Shared</p>
                    <p>${board.hidden ? 'Hidden' : 'Not Hidden'}</p>
                    <p><a href="/board/${board.id}/edit">Edit</a></p>
                </c:otherwise>
            </c:choose>
            <hr/>
        </div>
    </c:forEach>
</hcs:standard-page>