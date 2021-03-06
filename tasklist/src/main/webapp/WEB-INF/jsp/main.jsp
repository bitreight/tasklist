<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta content="text/html; charset=utf-8">
        <title>Tasklist</title>
        <link href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendor/css/bootstrap-datepicker3.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendor/css/bootstrap-select.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    </head>

    <body>
        <div id="wrapper" class="background-main">
            <div id="header-main" class="header-style">
                <jsp:include page="header.jsp"/>
            </div>

            <div id="content">
                <!-- Common alert -->
                <div id="common-alert" class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    <span class="sr-only">Error:</span>
                    <span id="error-msg"></span>
                </div>

                <div id="left-menu-col">
                    <ul id="main-menu" class="list-group">
                        <li id="all-tasks" class="menu-item list-group-item menu-item-selected">ALL</li>
                        <li id="today-tasks" class="menu-item list-group-item">TODAY</li>
                        <li id="week-tasks" class="menu-item list-group-item">WEEK</li>
                        <li class="list-group-item">
                            PROJECTS
                            <span id="add-project" class="glyphicon glyphicon-plus pull-right"></span>
                        </li>
                    </ul>
                    <ul id="project-list" class="list-group">
                        <!-- projects of the user -->
                    </ul>
                </div>

                <!-- Project edit modal window -->
                <div class="modal fade" id="project-details-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
                                <h4 class="modal-title" id="project-modal-title"></h4>
                            </div>
                            <div class="modal-body">
                                <form id="project-details-form">
                                    <div id="project-save-error" class="alert alert-danger project-error" role="alert">
                                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                        <span class="sr-only">Error:</span>
                                    </div>
                                    <input id="project-id" type="hidden"/>
                                    <input id="project-title" type="text" class="form-control" placeholder="Title"/>
                                    <div id="project-title-error" class="alert alert-danger project-error" role="alert">
                                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                        <span class="sr-only">Error:</span>
                                    </div>
                                    <textarea id="project-description" class="form-control" rows="6" placeholder="Description"></textarea>
                                    <div id="project-description-error" class="alert alert-danger project-error" role="alert">
                                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                        <span class="sr-only">Error:</span>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                <button id="save-project" type="submit" class="btn btn-primary">Save</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Project delete modal window -->
                <div class="modal fade" id="project-delete-confirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-sm" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                Are you sure you want to delete project?
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                <button id="delete-project" data-project-id="" type="submit" class="btn btn-primary">Delete</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="tasks-column">
                    <%--<div id="tasks-content">--%>
                    <div id="tasks-content" class="panel panel-default">
                        <div class="panel-heading">
                            <div class="input-group">
                                <input id="task-title" type="text" class="form-control" placeholder="Enter task name...">
                                <span class="input-group-btn">
                                    <button id="add-task" data-project-id="" class="btn btn-default" type="button">Add task</button>
                                </span>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div id="sort-dropdown" class="dropdown">
                                <button class="btn btn-default dropdown-toggle" type="button" id="task-sort" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                    Sort by
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu pull-right" aria-labelledby="task-sort">
                                    <li data-sort-type="title"><a href="">Title</a></li>
                                    <li data-sort-type="deadline"><a href="">Deadline</a></li>
                                    <li data-sort-type="priority"><a href="">Priority</a></li>
                                </ul>
                            </div>
                        </div>
                        <ul id="task-list" class="list-group">
                            <!-- tasks of user or project -->
                        </ul>
                    </div>
                    <%--</div>--%>

                    <div id="task-details">
                        <form id="task-details-form">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="input-group">
                                        <input id="task-name-edit" type="text" class="form-control" placeholder="Task name">
                                        <span class="input-group-btn">
                                            <button id="save-task" class="btn btn-default" type="button">Save</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <!-- Field errors -->
                                    <!-- Title error -->
                                    <div id="title-error" class="task-field-error alert alert-danger" role="alert">
                                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                        <span class="sr-only">Error:</span>
                                        <span id="title-error-msg"></span>
                                    </div>
                                    <!-- Deadline error -->
                                    <div id="deadline-error" class="task-field-error alert alert-danger" role="alert">
                                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                        <span class="sr-only">Error:</span>
                                        <span id="deadline-error-msg"></span>
                                    </div>
                                    <!-- Description error -->
                                    <div id="description-error" class="task-field-error alert alert-danger" role="alert">
                                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                        <span class="sr-only">Error:</span>
                                        <span id="description-error-msg"></span>
                                    </div>

                                    <div id="priority-select" class="form-group pull-right">
                                        <select class="selectpicker show-menu-arrow form-control">
                                            <option value="0">High</option>
                                            <option value="1" selected>Normal</option>
                                            <option value="2">Low</option>
                                        </select>
                                    </div>
                                    <input type="text" class="form-control pull-right" id="datepicker" placeholder="Deadline" />
                                    <textarea id="task-description-edit" class="form-control pull-left" rows="6" placeholder="Description"></textarea>
                                    <input id="task-version" type="hidden" />
                                    <input id="task-id" type="hidden"/>
                                    <input id="task-is-completed" type="hidden"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/vendor/jquery/jquery-3.1.1.min.js"/>"></script>
        <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/resources/vendor/js/bootstrap-datepicker.min.js"/>"></script>
        <script src="<c:url value="/resources/vendor/js/bootstrap-select.min.js"/>"></script>
        <script src="<c:url value="/resources/vendor/js/moment.js"/>"></script>
        <script src="<c:url value="/resources/js/main.js"/>"></script>

        <script>
            $(document).ready(function () {
                var today = new Date();
                $("#datepicker").datepicker({
                    format: "dd-mm-yyyy",
                    viewMode: "days",
                    minViewMode: "days"
                    //endDate: today.getFullYear().toString()
                });
            });
        </script>
    </body>

</html>
