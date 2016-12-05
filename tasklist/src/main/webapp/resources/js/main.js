$(document).ready(function () {

    var projectEditMode = false;
    var projectsApiBaseUrl = "/tasklist/api/projects";

    getUserProjects();

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
                $("#project-description").val(project.title);
            }
        });
    }

    $("#project-list")
        .on("click", ".delete-project", function () {
            var projectItem = $(this).parent();
            var projectId = projectItem.data("project-id");
            
            $("#project-delete-confirm").modal("show");
    
            // $.ajax({
            //     url: projectsApiBaseUrl + "/" + projectId,
            //     type: "DELETE",
            //     success: function () {
            //         projectItem.remove();
            //     }
            // });

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


    /*----- tasks functionality -----*/

});