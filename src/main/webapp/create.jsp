<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes.jsp" %>

<hcs:standard-page title="Create new board" page="create">
    <hcs:form method="post" action="/create">
        <div>
            <label>
                Soundboard Title
                <input type="text" name="title" required="required"/>
            </label>
        </div>
        <div>
            <label>
                Soundboard Description
                <input type="text" name="description"/>
            </label>
        </div>

        <input type="submit" value="Create a New Soundboard"/>
    </hcs:form>
</hcs:standard-page>