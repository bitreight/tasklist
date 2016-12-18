$(document).ready(function () {

    var apiBaseUrl = "/tasklist/api";
    var projectsApiBaseUrl = apiBaseUrl+ "/projects";
    var tasksApiBaseUrl = apiBaseUrl+ "/tasks";

    /*----- projects functionality -----*/
    var projectEditMode = false;
    getUserProjects();

    function getUserProjects() {
        $.ajax({
            url: projectsApiBaseUrl,
            type: "GET",
            success: function (projects) {
                var projectList = $("#project-list");
                projectList.html('');

                projects.forEach(function (project, i , projects) {
                    var projectItem = $('<li class="menu-item list-group-item">' +
                                            '<span class="project-title"></span>' +
                                            '<span class="delete-project glyphicon glyphicon-trash pull-right"></span>' +
                                            '<span class="edit-project glyphicon glyphicon-pencil pull-right"></span>' +
                                        '</li>');
                    projectItem.attr("data-project-id", project.id);
                    projectItem.find(".project-title").text(project.title);
                    projectList.append(projectItem);
                });

                selectActiveProject();
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

                $("#project-details-modal").modal("show");
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                showCommonError(errorMessage);
            }
        });
    }
    
    function deleteProject(projectId, projectItem) {
        $.ajax({
            url: projectsApiBaseUrl + "/" + projectId,
            type: "DELETE",
            success: function () {
                projectItem.remove();

                var currentProjectId = $("#add-task").data("project-id");
                if(projectId === currentProjectId || !currentProjectId) {
                    document.getElementById("all-tasks").click();
                }
            }, error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                showCommonError(errorMessage);
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
    });
    
    $("#add-project").click(function () {
        projectEditMode = false;
        $("#project-modal-title").text("Create project");
        $("#project-details-modal").modal("show");
    });

    $("#save-project").click(function () {
        var apiUrl = projectsApiBaseUrl;
        var projectJson = { "id": null, "title": null, "description": null };
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

    
    /*------------- tasks functionality -----------*/
    var getTasks = createGetTasks();
    getUserTasks();
    toggleTaskAddControls(false);

    var taskJson = {
        "id": null,
        "title": null,
        "description": null,
        "deadline": null,
        "priority": 1,
        "completed": false,
        "version": 0
    };

    function getUserTasks() {
        getTasks(tasksApiBaseUrl);
    }

    function getTodayUserTasks() {
        var apiUrl = tasksApiBaseUrl+ "/today";
        var currentDate = moment().format("DD-MM-YYYY");
        getTasks(apiUrl, currentDate);
    }

    function getWeekUserTasks() {
        var apiUrl = tasksApiBaseUrl + "/week";
        var currentDate = moment().format("DD-MM-YYYY");
        getTasks(apiUrl, currentDate);
    }

    function getProjectTasks(projectId) {
        var apiUrl = apiBaseUrl+ "/projects/" + projectId + "/tasks";
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

                    //toggleTaskAddControls(true);
                    toggleSortMenu(tasks.length);

                    tasks.forEach(function (task, i, tasks) {
                        var taskItem = $('<li class="list-group-item">' +
                                            '<input class="check-as-completed" type="checkbox"/>' +
                                            '<span class="task-title"></span>' +
                                            '<span class="delete-task glyphicon glyphicon-trash pull-right"></span>' +
                                            '<span class="task-deadline pull-right"></span>' +
                                        '</li>');
                        var prioritySpanHigh = $('<span class="high-prior glyphicon glyphicon-arrow-up pull-right"></span>');
                        var prioritySpanLow = $('<span class="low-prior glyphicon glyphicon-arrow-down pull-right"></span>');

                        taskItem.attr("data-task-id", task.id);
                        taskItem.find(".task-title").text(task.title);

                        var deadlineSpan = taskItem.find(".task-deadline");
                        deadlineSpan.text(task.deadline);

                        if(task.priority === 0) {
                            deadlineSpan.after(prioritySpanHigh);
                        } else if(task.priority === 2) {
                            deadlineSpan.after(prioritySpanLow);
                        }

                        if(task.completed) {
                            taskItem.find(".task-title").addClass("completed");
                            taskItem.find(".check-as-completed").prop("checked", true);
                        }

                        taskList.append(taskItem);
                    });

                }, error: function (errorResponse) {
                    var errorMessage = errorResponse.responseJSON.message;
                    showCommonError(errorMessage);
                }
            });
        }

        return getTasksClosure;
    }

    function getTaskDetails(taskId) {
        var apiBaseUrl = tasksApiBaseUrl + "/" + taskId;
        $.ajax({
            url: apiBaseUrl,
            type: "GET",
            success: function (task) {
                $("#task-id").val(task.id);
                $("#task-name-edit").val(task.title);
                $("#datepicker").val(task.deadline);

                $(".selectpicker").val(task.priority);
                $('.selectpicker').selectpicker('refresh');

                $("#task-description-edit").val(task.description);
                $("#task-is-completed").val(task.completed);
                $("#task-version").val(task.version);

                showTaskForm();
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                showCommonError(errorMessage);
                hideTaskForm();
                getTasks();
            }
        });
    }

    function addTask(taskJson, projectId) {
        var apiUrl = apiBaseUrl + "/projects/" + projectId + "/tasks";
        $.ajax({
            url: apiUrl,
            type: "POST",
            data: JSON.stringify(taskJson),
            contentType: "application/json",
            success: function () {
                $("#task-title").val("");
                getTasks();
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                var fieldErrors = errorResponse.responseJSON.fieldErrors;

                if(errorMessage) {
                    showCommonError(errorMessage);
                }
                if(fieldErrors) {
                    showCommonError(fieldErrors.title);
                }
            }
        });
    }

    function updateTask(taskJson, taskId) {
        var apiUrl = tasksApiBaseUrl + "/" + taskId;
        $.ajax({
            url: apiUrl,
            type: "PUT",
            data: JSON.stringify(taskJson),
            contentType: "application/json",
            success: function () {
                getTaskDetails(taskId);
                getTasks();
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                var fieldErrors = errorResponse.responseJSON.fieldErrors;

                if(errorMessage) {
                    showCommonError(errorMessage);
                    hideTaskForm();
                    getTasks();
                }

                if(fieldErrors) {
                    var priorityError = fieldErrors.priority;
                    var versionError = fieldErrors.version;

                    if(versionError) {
                        showCommonError(versionError);
                        hideTaskForm();
                        getTasks();

                    } else if(priorityError) {
                        showCommonError(priorityError);
                        hideTaskForm();
                        getTasks();

                    } else {
                        showTaskErrors(fieldErrors);
                    }
                }
            }
        });
    }

    function deleteTask(taskId, taskItem) {
        var apiUrl = tasksApiBaseUrl + "/" + taskId;
        $.ajax({
            url: apiUrl,
            type: "DELETE",
            success: function () {
                getTasks();
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                showCommonError(errorMessage);
                hideTaskForm();
                getTasks();
            }
        });
    }
    
    function setTaskCompleted(taskId, taskItem, isCompleted) {
        var apiUrl = tasksApiBaseUrl + "/" + taskId + "?is_completed=" + isCompleted;
        $.ajax({
            url: apiUrl,
            type: "PATCH",
            success: function () {
                $(taskItem).find(".task-title").toggleClass("completed");

                if(taskId == $("#task-id").val()) {
                    getTaskDetails(taskId);
                }
            },
            error: function (errorResponse) {
                var errorMessage = errorResponse.responseJSON.message;
                showCommonError(errorMessage);
                hideTaskForm();
                getTasks();
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

    $("#project-list").on("click", "li", function (e) {
        var titleSpan = $(this).find(".project-title")[0];
        if(e.target === this || e.target === titleSpan) {
            var projectId = $(this).data("project-id");
            $("#add-task").data("project-id", projectId);

            getProjectTasks(projectId);
            toggleTaskAddControls(true);
        }
    });

    $("#add-task").click(function () {
        var taskToAdd = JSON.parse(JSON.stringify(taskJson));

        taskToAdd.title = $("#task-title").val();
        var projectId = $("#add-task").data("project-id");

        addTask(taskToAdd, projectId);
    });

    $("#save-task").click(function () {
        var taskToUpdate = JSON.parse(JSON.stringify(taskJson));

        taskToUpdate.id = $("#task-id").val();
        taskToUpdate.title = $("#task-name-edit").val();
        taskToUpdate.description = $("#task-description-edit").val();
        taskToUpdate.deadline = $("#datepicker").val() || null;
        taskToUpdate.priority = $(".selectpicker").val();
        taskToUpdate.completed = JSON.parse($("#task-is-completed").val());
        taskToUpdate.version = $("#task-version").val();

        updateTask(taskToUpdate, taskToUpdate.id);
    });

    $("#task-list")
        .on("click", "li", function (e) {
            var titleSpan = $(this).find(".task-title")[0];
            if(e.target === this || e.target === titleSpan) {
                var taskId = $(this).data("task-id");
                getTaskDetails(taskId);
            }
        })
        .on("click", ".delete-task", function () {
            var taskItem = $(this).parent();
            var taskId = $(taskItem).data("task-id");

            deleteTask(taskId, taskItem);
        })
        .on("click", ".check-as-completed", function () {
            var taskItem = $(this).parent();
            var taskId = $(taskItem).data("task-id");
            var isCompleted = $(this).prop("checked");

            setTaskCompleted(taskId, taskItem, isCompleted);
    });

    $("#sort-dropdown").on("click", "li", function (e) {
        var sort = $(this).data("sort-type");
        getTasks(null, null, sort);

        e.preventDefault();
    });


    /*-------------------- Behavior -----------------*/
    function selectActiveProject() {
        var projectId = $("#add-task").data("project-id");
        if(projectId) {
            var projectItem = $('li[data-project-id="' + projectId + '"]');
            projectItem.addClass("menu-item-selected");
        }
    }

    $("#left-menu-col").on("click", "li", function () {
            hideTaskForm();
        })
        .on("click", ".menu-item", function (e) {
            var titleSpan = $(this).find(".project-title")[0];
            if(e.target === this || e.target === titleSpan) {
                $(".menu-item").removeClass("menu-item-selected");
                $(this).addClass("menu-item-selected");
            }
        });

    $("#main-menu").on("click", ".menu-item", function () {
        toggleTaskAddControls(false)
    });

    function toggleTaskAddControls(enabled) {
        var addTaskButton = $("#add-task");
        var taskTitleInput = $("#task-title");

        if(enabled) {
            taskTitleInput.prop("disabled", false);
            addTaskButton.prop("disabled", false);

        } else {
            taskTitleInput.prop("disabled", true);
            addTaskButton.prop("disabled", true);
            addTaskButton.data("project-id", "");
            taskTitleInput.val("");
        }
    }
    
    function showTaskForm() {
        hideTaskErrors();
        $("#task-details").show();
    }

    function hideTaskForm() {
        $("#task-details-form input").val("");
        $("#task-details-form textarea").val("");
        $("#datepicker").val("");
        $(".selectpicker").val("");
        $("#task-details").hide();

        hideTaskErrors();
    }

    function toggleSortMenu(taskListSize) {
        var sortMenu = $("#task-sort");
        if(taskListSize < 1) {
            sortMenu.prop("disabled", true);
        } else {
            sortMenu.prop("disabled", false);
        }
    }

    function showCommonError(message) {
        var errorDiv = $("#common-alert");
        $("#error-msg").text(message);

        errorDiv.fadeIn(600);
        setTimeout(function () {
            errorDiv.fadeOut(600);
        }, 3000);
        setTimeout(function () {
            $("#error-msg").text("");
        }, 3600);
    }

    function showTaskErrors(fieldErrors) {
        var titleError, descriptionError, deadlineError;

        titleError = fieldErrors.title;
        descriptionError = fieldErrors.description;
        deadlineError = fieldErrors.deadline;

        hideTaskErrors();

        if(titleError) {
            $("#title-error-msg").text(titleError);
            $("#title-error").show();
        }

        if(descriptionError) {
            $("#description-error-msg").text(descriptionError);
            $("#description-error").show();
        }

        if(deadlineError) {
            $("#deadline-error-msg").text(deadlineError);
            $("#deadline-error").show();
        }
    }

    function hideTaskErrors() {
        $(".task-field-error").hide();
        $("#title-error-msg").text("");
        $("#deadline-error-msg").text("");
        $("#description-error-msg").text("");
    }
});