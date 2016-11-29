<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/workspace"/>">Tasklist</a>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="isAnonymous()">
                    <li><a href="<c:url value="/join"/>">Sign up</a></li>
                    <li><a href="<c:url value="/login"/>">Login</a></li>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <li><a href="<c:url value="/profile"/>"><sec:authentication property="principal.username"/></a></li>
                    <li><a href="<c:url value="/logout"/>">Logout</a></li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>