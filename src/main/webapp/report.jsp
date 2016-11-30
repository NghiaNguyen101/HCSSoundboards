<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="report" type="com.hcs.soundboard.data.Report"--%>
<hcs:standard-page title="Report ${report.reportId}" page="board">
    <div class="jumbotron">
        <h1>Report #<c:out value="${report.reportId}"/></h1>
        <p class="lead">Date: <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${report.reportDate}"/></p>
        <p class="lead">By <hcs:user username="${report.reportUser}"/></p>
        <p class="lead">For board <a href="/board/${report.boardId}">${report.boardTitle}</a>
            created by <hcs:user username="${report.boardOwner}"/></p>
    </div>
    <div>
        <h4>Detail:</h4>
        <textarea class="form-control" disabled="disabled" rows="3" style="resize: none">${report.reportDesc}</textarea>
    </div>

    <hcs:form action="/report/${report.reportId}/resolve" method="post" id="resolved_report_form">
    </hcs:form>

    <h4>Notes:</h4>
    <hcs:form id="save_notes_report_form" action="/report/${report.reportId}/save_notes" method="post">
        <input id="original_notes" type="hidden" value="${report.notes}">
        <textarea id="report_notes" class="form-control" name="notes"
                  style="resize: none" rows="3" maxlength="512">${report.notes}</textarea>
    </hcs:form>

    <br />
    <!-- confirm box for resloving report -->
    <div style="text-align: center">
        <button type="button" class="btn btn-lg btn-success" id="save_notes_button">Save Notes</button> &nbsp;
        <button type="button" class="btn btn-warning btn-lg" data-toggle="modal" data-target="#confirm_resolved_modal">Resolve</button>
    </div>
    <div class="modal fade" id="confirm_resolved_modal" role="dialog" style="display:none;">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Resolve this report?</h4>
                </div>
                <div class="modal-body" style="text-align: center">
                    <br />
                   <button type="button" class="btn btn-lg btn-danger" id="confirm_resolved">Yes</button> &nbsp;&nbsp;
                    <button type="button" class="btn btn-lg btn-warning" id="cancel_resolved" data-dismiss="modal">No</button>
                    <br /> <br />
                </div>
            </div>
        </div>
    </div>

</hcs:standard-page>
