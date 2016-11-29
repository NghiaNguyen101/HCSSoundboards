<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>HCS 404</title>
</head>
<body>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container main">
        <div class="navbar-header">
            <a class="navbar-brand logo" href="/"><img id="logo" src="https://s14.postimg.org/9y58owu0x/hcs.png"><!--HCS Soundboards--></a>
        </div>
    </div>
</nav>
    <div class="jumbotron">
        <h1>Oops, We couldn't find that page</h1>
        <p class="lead"> Error: ${msg}</p>
        <h2>Try searching or go back to <a href="/">HCSSoundboards</a> home page</h2>
        <br>
    </div>
</body>
