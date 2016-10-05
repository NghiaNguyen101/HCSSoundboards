<%@ attribute name="page" type="java.lang.String" required="false" %>

<%@ include file="/includes.jsp" %>

<nav class="navbar navbar-default navbar-static-top">
    <div class="container main">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">HCS Soundboards</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="${page == 'home' ? 'active' : ''}"><a href="/">Home</a></li>
                <li class="${page == 'your-boards' ? 'active' : ''}"><a href="/your-boards">Your Boards</a></li>
                <li class="${page == 'create' ? 'active' : ''}"><a href="/create">Create</a></li>
            </ul>
            <hcs:form name="logout" action="/logout" method="post">
                <ul class="nav navbar-nav navbar-right">
                    <c:choose>
                        <c:when test="${user.member}">
                            <li><a href="/account"><c:out value="${user.username}'s Account"/></a></li>
                            <li><a href="javascript:document.forms['logout'].submit()">Sign Out</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="/login">Sign In</a></li>
                            <li><a href="/register">Sign Up</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </hcs:form>
        </div><!--/.nav-collapse -->
    </div>
</nav>