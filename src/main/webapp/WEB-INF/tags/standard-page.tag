<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="page" type="java.lang.String" required="false" %>

<%@ include file="/includes.jsp" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="HCS Soundboards">
    <meta name="author" content="HCS">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-social/4.2.1/bootstrap-social.min.css">
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    <title>${title}</title>
</head>

<body>
<hcs:navbar page="${page}"/>
<div class="container">
    <jsp:doBody/>
    <hcs:footer/>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</body>
</html>