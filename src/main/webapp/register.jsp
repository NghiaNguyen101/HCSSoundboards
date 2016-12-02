<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Sign up" page="register">
    <div class="jumbotron">
        <p class="lead">Sign up for an account</p>
    </div>

    <div class="form-signin">
        <div class="hidden alert alert-danger fade in" id="register_warning">
            <li id="nameTaken" class="hidden"></li>
            <li id="pass_length_error" class="hidden"></li>
            <li id="pass_not_match" class="hidden"></li>
        </div>
        <hcs:form action="/register" method="post" id="register">
            <div class="form-group">
                <input type="text" class="form-control" maxlength="45"
                       placeholder="Username" name="username" id="username"
                       required="required" autocomplete="off" autofocus/>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" maxlength="60"
                       placeholder="Password" name="password" id="password"
                       required="required" autocomplete="off"/>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" maxlength="60"
                       placeholder="Confirm Password" name="confirm-password" id="confirm-password"
                       required="required" autocomplete="off"/>
            </div>
            <div><input type="submit" value="Sign Up" class="btn btn-primary"/></div>
        </hcs:form>
    </div>
</hcs:standard-page>
