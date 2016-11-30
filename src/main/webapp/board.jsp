<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<%--@elvariable id="board" type="com.hcs.soundboard.data.Board"--%>
<%--@elvariable id="useShared" type="java.lang.Boolean"--%>
<%--@elvariable id="user" type="com.hcs.soundboard.data.HCSUser"--%>
<c:set var="version" value="${useShared ? board.sharedVersion : board.unsharedVersion}" />

<hcs:standard-page title="${version.title}" page="board">
    <div class="jumbotron">
        <h1><c:out value="${version.title}"/></h1>
        <p class="lead"><c:out value="${version.description}"/></p>
        <p class="lead">By <hcs:user username="${board.ownerName}"/></p>
    </div>
    <div id="buttons-view">
        <c:forEach var="sound" items="${version.sounds}">
            <hcs:sound-button sound="${sound}" />
        </c:forEach>
    </div>
    <c:if test="${user.username == board.ownerName}">
        <p><a href="/board/${board.id}/edit">Edit</a></p>
    </c:if>
    <c:if test="${user.username != board.ownerName && user.username != null}">
        <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Report</button>

        <!-- Modal -->
        <div class="modal fade" id="myModal" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Report this soundboard</h4>
                    </div>
                    <div class="modal-body">
                        <hcs:form method="post" enctype="multipart/form-data" action="/board/${board.id}/create-report"
                            id="submit_report">
                            <h4>Tell us about it, what happen?</h4>
                            <input type="radio" name="reportDesc" value="Sexual Content" id="report_1" checked>
                            <label for="report_1"> Sexual Content</label><br />
                            <input type="radio" name="reportDesc" value="Violent and abusive content" id="report_2">
                            <label for="report_2"> Violent and abusive content</label><br />
                            <input type="radio" name="reportDesc" value="Spam or misleading" id="report_3" >
                            <label for="report_3"> Spam or misleading</label><br />
                            <input type="radio" name="reportDesc" value="Copyright issues" id="report_4">
                            <label for="report_4"> Copyright issues</label><br />
                            <input type="radio" name="reportDesc" value="other" id="other_report">
                            <label for="other_report"> Other, please explain: </label>
                            <input class="form-control" type="text" name="other_detail" id="other_report_text"
                                   disabled="disabled">
                            <br/>
                            <input class="btn btn-info btn-lg center" type="submit" value="Send Report"/>
                        </hcs:form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</hcs:standard-page>