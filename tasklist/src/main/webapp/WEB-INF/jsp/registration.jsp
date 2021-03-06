<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
    <head>
        <meta content="text/html; charset=utf-8">
        <title>Sign up</title>
        <link href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    </head>

    <body>
        <div class="wrapper">
            <div id="header" class="header-style">
                <jsp:include page="header.jsp"/>
            </div>
            <div id="registration-form-wrapper" class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
                <c:url value="/join" var="actionUrl"/>
                <form:form modelAttribute="userDto" action="${actionUrl}" method="post" id="registration-form">
                    <h2 class="form-signin-heading">Create an account</h2>
                    <c:if test="${error != null}">
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                            <span class="sr-only">Error:</span>
                            ${error}
                        </div>
                    </c:if>
                    <input id="input-login" type="text" class="form-control" name="username" placeholder="Login" autofocus="" required/>
                    <spring:bind path="username">
                        <c:if test="${status.error}">
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <form:errors path="username"/>
                            </div>
                        </c:if>
                    </spring:bind>

                    <input id="input-password" type="password" class="form-control" name="password" placeholder="Password" required/>
                    <spring:bind path="password">
                        <c:if test="${status.error}">
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <form:errors path="password"/>
                            </div>
                        </c:if>
                    </spring:bind>

                    <input id="input-name" type="text" class="form-control" name="name" placeholder="Name"/>
                    <spring:bind path="name">
                        <c:if test="${status.error}">
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <form:errors path="name"/>
                            </div>
                        </c:if>
                    </spring:bind>

                    <input id="input-surname" type="text" class="form-control" name="surname" placeholder="Surname"/>
                    <spring:bind path="surname">
                        <c:if test="${status.error}">
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <form:errors path="surname"/>
                            </div>
                        </c:if>
                    </spring:bind>
                    <button id="button-register" class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
                </form:form>

            </div>
        </div>

    <script src="<c:url value="/resources/vendor/jquery/jquery-3.1.1.min.js"/>"></script>
    <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
    </body>
</html>
