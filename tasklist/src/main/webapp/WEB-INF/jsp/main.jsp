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
                <div id="left-menu-col">
                    <div class="list-group">
                        <a href="#" class="list-group-item">ALL</a>
                        <a href="#" class="list-group-item">TODAY</a>
                        <a href="#" class="list-group-item">WEEK</a>
                        <div class="list-group-item">
                            PROJECTS
                            <span id="add-project" class="glyphicon glyphicon-plus"></span>
                        </div>
                        <div id="project-list">
                            <a href="#" class="list-group-item">
                                project 1
                                <span id="delete-project" class="glyphicon glyphicon-trash pull-right"></span>
                            </a>
                        </div>
                    </div>
                </div>

                <div id="tasks-content">
                    <div id="task-list">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Enter task name...">
                                        <span class="input-group-btn">
                                            <button class="btn btn-default" type="button">Add task</button>
                                        </span>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div id="sort-dropdown" class="dropdown">
                                    <button class="btn btn-default dropdown-toggle" type="button" id="task-sort" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                        Sort by
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" aria-labelledby="task-sort">
                                        <li><a href="#">Name</a></li>
                                        <li><a href="#">Deadline</a></li>
                                        <li><a href="#">Priority</a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="list-group">
                                <a href="#" class="list-group-item">
                                    <input id="check-as-completed" type="checkbox"/>
                                    Task 1
                                    <span id="delete-task" class="glyphicon glyphicon-trash"></span>
                                </a>
                            </div>
                        </div>
                    </div>

                    <div id="task-details">
                        <form id="task-details-form">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <input id="task-name-edit" type="text" class="form-control">
                                </div>
                                <div class="panel-body">
                                    <div id="priority-select" class="form-group pull-left">
                                        <select class="selectpicker show-menu-arrow form-control">
                                            <option>High</option>
                                            <option selected>Normal</option>
                                            <option>Low</option>
                                        </select>
                                    </div>
                                    <input type="text" class="form-control pull-left" id="datepicker" placeholder="Deadline" />
                                    <button id="task-save-button" type="button" class="btn btn-default pull-left">Save</button>
                                    <textarea id="task-description-edit" class="form-control pull-left" rows="6">Cool task</textarea>
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

        <script>
            $(document).ready(function () {
                var today = new Date();
                $("#datepicker").datepicker({
                    format: "dd-mm-yyyy",
                    viewMode: "days",
                    minViewMode: "days",
                    endDate: today.getFullYear().toString()
                });
            });
        </script>
    </body>

</html>