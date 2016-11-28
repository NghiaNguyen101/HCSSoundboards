<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<hcs:standard-page title="Report ${report.reportId}" page="board">
    <div class="jumbotron">
        <h1><c:out value="${report.reportTitle}"/></h1>
        <p class="lead">Date: <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${report.reportDate}"/></p>
        <p class="lead">By <hcs:user username="${report.reportUser}"/></p>
        <p class="lead">For board <a href="/board/${report.boardId}">${report.boardTitle}</a>
            created by <hcs:user username="${report.boardOwner}"/></p>
    </div>
    <div>
        <h4>Detail:</h4>
        <textarea class="form-control" disabled="disabled" rows="4" style="resize: none">${report.reportDesc}"</textarea>
    </div>

    <hcs:form action="/report/${report.reportId}/resolved" method="post" id="resolved_report_form">
       <!-- <input class="btn btn-warning" type="submit" value="Resolved" /> -->
    </hcs:form>

    <h4>Notes:</h4>
    <hcs:form id="save_notes_report_form" action="/report/${report.reportId}/save_notes" method="post">
        <input id="oroginal_notes" type="hidden" value="${report.notes}">
        <textarea id="report_notes" class="form-control" name="notes" style="resize: none" rows="4">${report.notes}</textarea>
    </hcs:form>
    <!--<input type="hidden" id="report_id" value="${report.reportId}"> -->
    <p id="warning_save_notes" class="hidden bg-warning text-danger">Your notes is updated. Please save your notes!</p>

    <br />
    <!-- confirm box for resloving report -->
    <div style="text-align: center">
        <button type="button" class="btn btn-lg btn-success" id="save_notes_button">Save Notes</button> &nbsp
        <button type="button" class="btn btn-warning btn-lg" data-toggle="modal" data-target="#confirm_resolved_modal">Resolved</button>
    </div>
    <div class="modal fade" id="confirm_resolved_modal" role="dialog" style="display:none;">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Resolved this report?</h4>
                </div>
                <div class="modal-body" style="text-align: center">
                    <br />
                   <button type="button" class="btn btn-lg btn-danger" id="confirm_resolved">Yes</button> &nbsp&nbsp
                    <button type="button" class="btn btn-lg btn-warning" id="cancel_resolved" data-dismiss="modal">No</button>
                    <br /> <br />
                    <p>Please be careful!!!</p>
                </div>
            </div>
        </div>
    </div>

</hcs:standard-page>
