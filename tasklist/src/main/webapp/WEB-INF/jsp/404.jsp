<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Not found</title>
        <link href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    </head>
    <body>
    <div id="header" class="header-style">
        <jsp:include page="header.jsp"/>
    </div>
        <div id="error-header" class="page-header">
            <h1>The resource you are looking for is not found!<small><a href="<c:url value="/workspace"/>">Go to main page</a></small></h1>
        </div>

        <script src="<c:url value="/resources/vendor/jquery/jquery-3.1.1.min.js"/>"></script>
        <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
    </body>
</html>
