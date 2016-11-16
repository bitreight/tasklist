<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
    <head>
        <meta content="text/html; charset=utf-8">
        <title>Log in</title>
        <link href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    </head>

    <body>
        <div class="wrapper">
            <div id="login-form-wrapper" class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
                <form:form modelAttribute="userDto" action="login" method="post" id="login-form">
                    <h2 class="form-signin-heading">Please login</h2>
                    <input id="input-login" type="text" class="form-control" name="username" placeholder="Login" autofocus="" />
                    <spring:bind path="username">
                        <c:if test="${status.error}">
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <form:errors path="username"/>
                            </div>
                        </c:if>
                    </spring:bind>

                    <input id="input-password" type="password" class="form-control" name="password" placeholder="Password"/>
                    <spring:bind path="password">
                        <c:if test="${status.error}">
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <form:errors path="password"/>
                            </div>
                        </c:if>
                    </spring:bind>

                    <label class="checkbox">
                        <input type="checkbox" value="remember-me" id="rememberMe" name="rememberMe"> Remember me
                    </label>
                    <button id="button-login" class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
                </form:form>
                <a href="join">Create an account</a>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
    </body>
</html>
