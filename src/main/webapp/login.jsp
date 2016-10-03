<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes.jsp" %>

<hcs:standard-page title="Sign in" page="login">
    <div class="jumbotron">
        <p class="lead">Sign in to your HCS account.</p>
    </div>
    <div>
        <c:if test="${param.error != null}">
            Invalid username or password.
        </c:if>
        <hcs:form action="/login" method="post">
            <div><label> User Name : <input type="text" name="username"/> </label></div>
            <div><label> Password: <input type="password" name="password"/> </label></div>
            <div><input type="submit" value="Sign In"/></div>
        </hcs:form>
    </div>
    <div>
        Don't have an account yet? <a href="/register">Sign up</a> now!
    </div>
</hcs:standard-page>