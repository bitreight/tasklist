$(document).ready(function () {

    var projectEditMode = false;
    var defaultSort;
    var projectsApiBaseUrl = "/tasklist/api/projects";

    getUserProjects();
    //getUserTasks();

    /*----- projects functionality -----*/
    function getUserProjects() {
        $.ajax({
            url: projectsApiBaseUrl,
            type: "GET",
            success: function (projects) {
                var projectList = $("#project-list");
                projectList.html('');

                projects.forEach(function (project, i , projects) {
                    var projectItem = $('<li class="list-group-item">' +
                                            '<span class="delete-project glyphicon glyphicon-trash pull-right"></span>' +
                                            '<span class="edit-project glyphicon glyphicon-pencil pull-right"></span>' +
                                        '</li>');
                    projectItem.attr("data-project-id", project.id);
                    projectItem.prepend(project.title);
                    projectList.append(projectItem);
                });
            }
        });
    }

    function getProjectDetails(projectId) {
        $.ajax({
            url: projectsApiBaseUrl + "/" + projectId,
            type: "GET",
            success: function (project) {
                $("#project-id").val(project.id);
                $("#project-title").val(project.title);
                $("#project-description").val(project.description);
            }
        });
    }
    
    function deleteProject(projectId, projectItem) {
        $.ajax({
            url: projectsApiBaseUrl + "/" + projectId,
            type: "DELETE",
            success: function () {
                projectItem.remove();
            }            
        }).always(function () {
            $("#project-delete-confirm").modal("hide");
        });        
    }

    $("#project-list")
        .on("click", ".delete-project", function () {
            var projectId = $(this).parent().data("project-id");

            $("#delete-project").data("project-id", projectId);
            $("#project-delete-confirm").modal("show");
            
        }).on("click", ".edit-project", function () {
            var projectId = $(this).parent().data("project-id");
            getProjectDetails(projectId);

            projectEditMode = true;
            $("#project-modal-title").text("Edit project");
            $("#project-details-modal").modal("show");
    });
    
    $("#add-project").click(function () {
        projectEditMode = false;
        $("#project-modal-title").text("Create project");
        $("#project-details-modal").modal("show");
    });

    $("#save-project").click(function () {
        var apiUrl = projectsApiBaseUrl;
        var projectJson = {"id": null, "title": null, "description": null};
        var ajaxType = "POST";

        if(projectEditMode) {
            projectJson.id = $("#project-id").val();
            apiUrl = apiUrl + "/" + projectJson.id;
            ajaxType = "PUT";
        }
        projectJson.title = $("#project-title").val();
        projectJson.description = $("#project-description").val();

        $(".project-error").hide();

        $.ajax({
            url: apiUrl,
            type: ajaxType,
            data: JSON.stringify(projectJson),
            contentType: "application/json",
            success: function () {
                $("#project-details-modal").modal("hide");
                getUserProjects();                
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                var fieldErrors = errorResponse.responseJSON.fieldErrors;

                if(errorMessage) {
                    showProjectSaveError(errorMessage);
                }
                if(fieldErrors) {
                    showProjectFieldErrors(fieldErrors);
                }
            }
        });
    });

    function showProjectFieldErrors(errors) {
        var titleError = errors.title;
        var descriptionError = errors.description;
        var errorDiv;
        
        if(titleError) {
            errorDiv = $("#project-title-error");
            errorDiv.text(titleError);
            errorDiv.show();
        }
        if(descriptionError) {
            errorDiv = $("#project-description-error");
            errorDiv.text(descriptionError);
            errorDiv.show();
        }
    }

    function showProjectSaveError(message) {
        var errorDiv = $("#project-save-error");
        errorDiv.text(message);
        errorDiv.show();
    }

    $("#project-details-modal").on("hide.bs.modal", function () {
        $("#project-details-form")[0].reset();
        
        var errorDivs = $(".project-error");
        errorDivs.hide();
        errorDivs.text("");
    });

    $("#delete-project").click(function () {
        var projectId = $("#delete-project").data("project-id");
        var projectItem = $('li[data-project-id="'+ projectId +'"]');
        deleteProject(projectId, projectItem);
    });

    
    /*----- tasks functionality -----*/
    var getTasks = createGetTasks();
    getUserTasks();

    function getUserTasks() {
        var apiUrl = "/tasklist/api/tasks";
        getTasks(apiUrl);
    }

    function getTodayUserTasks() {
        var apiUrl = "/tasklist/api/tasks/today";
        var currentDate = moment().format("DD-MM-YYYY");
        getTasks(apiUrl, currentDate);
    }

    function getWeekUserTasks() {
        var apiUrl = "/tasklist/api/tasks/week";
        var currentDate = moment().format("DD-MM-YYYY");
        getTasks(apiUrl, currentDate);
    }

    function getProjectTasks(projectId) {
        var apiUrl = "/tasklist/api/projects/" + projectId + "/tasks";
        getTasks(apiUrl);
    }

    function addSortToUrl(url, sort) {
        return (sort) ? url.concat("?sort=", sort) : url;
    }
    
    function createGetTasks() {
        var apiUrlHolder, currentDateHolder, sortHolder;

        function getTasksClosure(apiUrl, currentDate, sort) {
            var argLen = arguments.length;
            
            if(argLen === 1) {
                apiUrlHolder = apiUrl;
                currentDateHolder = null;
            } else if (argLen === 2) {
                apiUrlHolder = apiUrl;
                currentDateHolder = currentDate;
            } else if (argLen === 3) {
                sortHolder = sort;
            }

            var url = addSortToUrl(apiUrlHolder, sortHolder);

            $.ajax({
                url: url,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Client-Date", currentDateHolder);
                },
                success: function (tasks) {
                    var taskList = $("#task-list");
                    taskList.html('');

                    tasks.forEach(function (task, i, tasks) {
                        var taskItem = $('<li class="list-group-item">' +
                            '<input class="check-as-completed" type="checkbox"/>' +
                            '<span class="delete-task glyphicon glyphicon-trash"></span>' +
                            '<span class="task-deadline"></span>' +
                            '</li>');
                        taskItem.attr("data-task-id", task.id);
                        taskItem.find("input").after(task.title);
                        taskItem.find(".task-deadline").text(task.deadline);
                        if(task.completed) {
                            taskItem.addClass("completed");
                            taskItem.find(".check-as-completed").prop("checked", true);
                        }
                        taskList.append(taskItem);
                    });
                }
            });
        }

        return getTasksClosure;
    }

    
    function saveTask(taskJson, projectId, taskId) {
        var apiUrl = "/tasklist/api/projects/" + projectId + "/tasks";
        $.ajax({
            url: apiUrl,
            type: "POST",
            data: JSON.stringify(taskJson),
            contentType: "application/json",
            success: function () {
                $("#task-title").val("");
                getTasks();
            },
            error: function (error) {
                //console.log(error);
            }
        });
    }

    function deleteTask(taskId, taskItem) {
        var apiUrl = "/tasklist/api/tasks/" + taskId;
        $.ajax({
            url: apiUrl,
            type: "DELETE",
            success: function () {
                $(taskItem).remove();
            }
        });
    }
    
    function setTaskCompleted(taskId, taskItem, isCompleted) {
        var apiUrl = "/tasklist/api/tasks/" + taskId + "?is_completed=" + isCompleted;

        $.ajax({
            url: apiUrl,
            type: "PATCH",
            success: function () {
                $(taskItem).toggleClass("completed");
            }     
        });
    }

    $("#all-tasks").click(function () {
        getUserTasks();
    });

    $("#today-tasks").click(function () {
        getTodayUserTasks();
    });

    $("#week-tasks").click(function () {
        getWeekUserTasks();
    });

    $("#project-list").on("click", "li", function () {
        var projectId = $(this).data("project-id");
        $("#save-task").data("project-id", projectId);
        getProjectTasks(projectId);
    });

    $("#add-task").click(function () {
        var taskJson = 
            {
                "id": null, 
                "title": null, 
                "description": null, 
                "deadline": null,
                "priority": 1,
                "isCompleted": false,
                "version": 0
            };
        
        taskJson.title = $("#task-title").val();
        var projectId = $("#save-task").data("project-id");
        saveTask(taskJson, projectId);
    });

    $("#task-list")
        .on("click", ".delete-task", function () {
            var taskItem = $(this).parent();
            var taskId = $(taskItem).data("task-id");
            deleteTask(taskId, taskItem);

        }).on("click", ".check-as-completed", function () {
            var taskItem = $(this).parent();
            var taskId = $(taskItem).data("task-id");
            var isCompleted = $(this).prop("checked");
            setTaskCompleted(taskId, taskItem, isCompleted);
    });

    $("#sort-dropdown").on("click", "li", function () {
        var sort = $(this).data("sort-type");
        getTasks(null, null, sort)
    });



    /*-------------------- Behavior -----------------*/
    $("#task-list").on("click", "li", function (e) {
        if(e.target === this) {
            $("#task-details").show();
        }
    });

    $("#left-menu-col").on("click", "li", function () {
        $("#task-details").hide();
    });

});