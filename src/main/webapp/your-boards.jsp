<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Your Boards" page="your-boards">
    <%--@elvariable id="boards" type="java.util.List<com.hcs.soundboard.data.Board>"--%>
    <c:forEach var="board" items="${boards}" varStatus="loop">
        <div>
            <c:if test="${loop.index != 0}">
                <hr />
            </c:if>
            <c:choose>
                <c:when test="${board.hasBeenShared()}">
                    <p style="font-size:24px"><a href="/board/${board.id}"><b><c:out value="${board.sharedVersion.title}"/></b></a></p>
                    <p style="font-size:18px"><b><c:out value="${board.sharedVersion.description}"/></b></p>
                    <p>Published</p>
                    <p>${board.hasUnsharedChanges() ? 'Unpublished Changes' : 'No Unpublished Changes'}</p>
                    <p>${board.hidden ? 'Hidden' : 'Not Hidden'}</p>
                    <p><a href="/board/${board.id}/edit">Edit</a></p>
                </c:when>
                <c:otherwise>
                    <p style="font-size:24px"><a href="/board/${board.id}/edit"><b><c:out value="${board.unsharedVersion.title}"/></b></a></p>
                    <p style="font-size:18px"><b><c:out value="${board.unsharedVersion.description}"/></b></p>
                    <p>Not Yet Published</p>
                    <p>${board.hidden ? 'Hidden' : 'Not Hidden'}</p>
                    <p><a href="/board/${board.id}/edit">Edit</a></p>
                </c:otherwise>
            </c:choose>
        </div>
    </c:forEach>
</hcs:standard-page>