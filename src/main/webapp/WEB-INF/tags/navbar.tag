<%@ attribute name="page" type="java.lang.String" required="false" %>

<%@ include file="/includes.jsp" %>

<%--@elvariable id="user" type="com.hcs.soundboard.data.HCSUser"--%>

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
            <a class="navbar-brand logo" href="/"><img id="logo" src="https://s14.postimg.org/9y58owu0x/hcs.png"><!--HCS Soundboards--></a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="${page == 'about' ? 'active' : ''}"><a href="/about">About Us</a></li>
                <li class="${page == 'create' ? 'active' : ''}"><a href="/create">Create</a></li>
                <li class="${page == 'browse' ? 'active' : ''}"><a href="/browse">Browse</a></li>
                <c:choose>
                    <c:when test="${user.member}">
                        <li class="${page == 'your-boards' ? 'active' : ''}"><a href="/your-boards">Your Boards</a></li>
                    </c:when>
                </c:choose>
            </ul>
            <hcs:form name="logout" action="/logout" method="post">
                <ul class="nav navbar-nav navbar-right">
                    <c:choose>
                        <c:when test="${user.member}">
                            <li><a href="/user/${user.username}"><c:out value="${user.username}'s Account"/></a></li>
                            <li><a href="javascript:document.forms['logout'].submit()">Sign Out</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="/login">Sign In</a></li>
                            <li><a href="/register">Sign Up</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </hcs:form>
        </div>
    </div>
</nav>