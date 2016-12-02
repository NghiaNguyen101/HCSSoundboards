<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Sign in" page="login">
    <div class="jumbotron">
        <p class="lead">Sign in to your HCS account.</p>
    </div>
    <div class="form-signin">
        <c:if test="${param.error != null}">
            <div class="alert alert-danger fade in">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                Invalid username or password.
            </div>
        </c:if>
        <hcs:form action="/login" method="post">
            <div class="form-group">
                <input type="text" class="form-control login"
                       placeholder="Username" name="username" maxlength="45"
                       required="required" autocomplete="off" autofocus/>
            </div>
            <div class="form-group">
                <input type="password" class="form-control login"
                       placeholder="Password" name="password" maxlength="60"
                       required="required" autocomplete="off"/>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" name="remember-me" id="remember-me"/>
                    Remember me
                </label>
            </div>
            <div class="form-group">
                <input type="submit" class="btn btn-primary" value="Sign in"/>
            </div>
        </hcs:form>
    </div>
    <div>
        <p id="fineprint">Don't have an account yet? <a href="/register">Sign up</a> now!</p>
    </div>
</hcs:standard-page>