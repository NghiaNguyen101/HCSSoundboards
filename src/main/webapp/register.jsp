<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Sign up" page="register">
    <div>
        <hcs:form action="/register" method="post" id="register">
            <div><label>Username:<input type="text" name="username" id="username" required="required"/></label></div>
            <div><label>Password:<input type="password" name="password" id="password" required="required"/> </label></div>
            <div><input type="submit" value="Sign Up" class="btn"/></div>
        </hcs:form>
        <div id="pass_error"></div>
        <c:if test="${param.taken != null}">
            Sorry, that username is already taken
        </c:if>
    </div>
</hcs:standard-page>
