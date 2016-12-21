<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
    <head>
        <meta content="text/html; charset=utf-8">
        <title>Profile</title>
        <link href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    </head>
    <body>
        <div class="wrapper">
            <div id="header" class="header-style">
                <jsp:include page="header.jsp"/>
            </div>

            <div id="profile-content">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-offset-3 col-lg-8 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-11 col-xs-offset-1 col-xs-10">
                            <div id="img-col">
                                <%--<c:if test="{userDto}"--%>
                                <img id="profile-image" src="" class="img-thumbnail" alt="Profile image" width="300" height="300">
                            </div>
                            <div id="info-col">
                                <div class="panel panel-default">
                                    <div class="panel-heading">Personal info</div>
                                    <table class="table">
                                        <tr>
                                            <td>Login:</td>
                                            <td id="username"></td>
                                        </tr>
                                        <tr>
                                            <td>Name:</td>
                                            <td id="name"></td>
                                        </tr>
                                        <tr>
                                            <td>Surname:</td>
                                            <td id="surname"></td>
                                        </tr>
                                    </table>
                                </div>
                                <button id="edit-user-info" class="btn btn-md btn-primary btn-block">Edit personal info</button>
                                <button id="change-password" class="btn btn-md btn-primary btn-block">Change password</button>
                                <button id="upload-image" class="btn btn-md btn-primary btn-block">Change profile image</button>
                                <div id="hideDefaultUpload">
                                    <input id="default-upload" type="file" accept="image/jpeg,image/png,image/gif" onchange="uploadProfileImage();" />
                                </div>
                            </div>
                        </div>

                        <!-- Change profile info modal -->
                        <div class="modal fade" id="user-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                            <div class="modal-dialog modal-sm" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <form id="user-info-form">
                                            <div id="user-save-error" class="alert alert-danger user-error" role="alert">
                                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                                <span class="sr-only">Error:</span>
                                            </div>

                                            <input id="user-name-input" type="text" class="form-control" placeholder="Name"/>
                                            <div id="name-error" class="alert alert-danger user-error" role="alert">
                                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                                <span class="sr-only">Error:</span>
                                            </div>

                                            <input id="user-surname-input" type="text" class="form-control" placeholder="Surname"/>
                                            <div id="surname-error" class="alert alert-danger user-error" role="alert">
                                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                                <span class="sr-only">Error:</span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                        <button id="save-user-info" type="submit" class="btn btn-primary">Save</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Change password modal -->
                        <div class="modal fade" id="password-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                            <div class="modal-dialog modal-sm" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <form id="password-form">
                                            <div id="password-change-error" class="alert alert-danger password-error" role="alert">
                                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                                <span class="sr-only">Error:</span>
                                            </div>
                                            <input id="password-input" type="password" class="form-control" placeholder="Old password" required/>

                                            <div id="password-confirm-error" class="alert alert-danger password-error" role="alert">
                                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                                <span class="sr-only">Error:</span>
                                            </div>
                                            <input id="new-password-input" type="password" class="form-control" placeholder="New password" required/>

                                            <div id="newpassword-error" class="alert alert-danger password-error" role="alert">
                                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                                <span class="sr-only">Error:</span>
                                            </div>

                                            <input id="password-confirm-input" type="password" class="form-control" placeholder="Confirm password" required/>

                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                        <button id="save-password" type="submit" class="btn btn-primary">Save</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/vendor/jquery/jquery-3.1.1.min.js"/>"></script>
        <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/resources/js/profile.js"/>"></script>
    </body>
</html>
