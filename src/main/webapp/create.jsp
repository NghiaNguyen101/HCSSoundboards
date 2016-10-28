<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Create new board" page="create">
    <hcs:form method="post" action="/create">
        <div class="jumbotron">
            <p class="lead">Create a Soundboard</p>
        </div>
        <div class="form-create">
            <div class="form-group">
                <input type="text" class="form-control"
                       name="title" placeholder="Soundboard Title"
                       required="required" autocomplete="off" autofocus/>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="Soundboard Description"
                          name="description">
            </div>
            <div class="form-group">
                <input type="submit" class="btn btn-primary" value="Create Soundboard"/>
            </div>
        </div>
    </hcs:form>
</hcs:standard-page>