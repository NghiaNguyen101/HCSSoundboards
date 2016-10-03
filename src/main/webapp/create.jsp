<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes.jsp" %>

<hcs:standard-page title="Create Soundboard" page="create">
    <div>
        <hcs:form method="post" enctype="multipart/form-data" action="/upload/sound">
            <input class="btn" type="file" accept="audio/*" name="sound"/>
            <input type="text" name="name" title="Name"/>
            <input class="btn" type="submit"/>
        </hcs:form>
    </div>
</hcs:standard-page>