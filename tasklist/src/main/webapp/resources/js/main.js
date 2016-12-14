$(document).ready(function () {

    var projectEditMode = false;
    var projectDeleteMode = false;
    var projectsApiBaseUrl = "/tasklist/api/projects";

    getUserProjects();
    getUserTasks();

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
    function getUserTasks(sort) {
        var apiUrl = "/tasklist/api/tasks";
        getTasks(apiUrl);
    }

    function getTodayUserTasks() {
        var apiUrl = "/tasklist/api/tasks/today";
        var currentDate = moment().format("MM-DD-YYYY");
        getTasks(apiUrl, currentDate);
    }

    function getWeekUserTasks() {
        var apiUrl = "/tasklist/api/tasks/week";        
        var currentDate = moment().format("MM-DD-YYYY");
        getTasks(apiUrl, currentDate);
    }

    function getProjectTasks(projectId, sort) {
        var apiUrl = "/tasklist/api/" + projectId + "/tasks";
        getTasks(apiUrl);
    }

    function getTasks(apiUrl, currentDate) {
        $.ajax({
            url: apiUrl,
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Client-Date", currentDate);
            },
            success: function (tasks) {
                var taskList = $("#task-list");
                taskList.html('');

                tasks.forEach(function (task, i, tasks) {
                    var listItem = $('<li class="list-group-item">' +
                                        '<input class="check-as-completed" type="checkbox"/>' +
                                        '<span class="delete-task glyphicon glyphicon-trash"></span>' +
                                        '<span class="task-deadline"></span>' +
                                     '</li>');
                    listItem.attr("data-task-id", task.id);
                    listItem.find("input").after(task.title);
                    listItem.find(".task-deadline").text(task.deadline);
                    taskList.append(listItem);
                });
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
    })

});