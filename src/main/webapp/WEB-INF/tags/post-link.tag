<%@ attribute name="action" type="java.lang.String" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>

<%@ include file="/includes.jsp" %>

<hcs:form action="${action}" name="${name}" method="post">
    <a href="javascript:document.forms['${name}'].submit()">
        <jsp:doBody />
    </a>
</hcs:form>