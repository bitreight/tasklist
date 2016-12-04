$(document).ready(function () {
    
    getUserProjects();

    function getUserProjects() {
        var baseUrl = "/tasklist/api/projects";

        $.ajax({
            url: baseUrl,
            type: "GET",
            success: function (projects) {
                var projectList = $("#project-list");

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

    $("#project-list")
        .on("click", ".delete-project", function () {
            var baseUrl = "/tasklist/api/projects/";
            var projectItem = $(this).parent();
    
            $.ajax({
                url: baseUrl + projectItem.data("project-id"),
                type: "DELETE",
                success: function () {
                    projectItem.remove();
                }
            });
        });
        // .on("click", ".edit-project", function () {
        //     $("#project-details-modal").modal("show");
        // });
    
    $("#add-project").click(function () {
        $("#project-details-modal").modal("show");
    });

    $("#save-project").click(function () {
        var baseUrl = "/tasklist/api/projects";

        var projectJson = {"id": null, "title": null, "description": null};
        projectJson.id = $("#project-id").val();
        projectJson.title = $("#project-title").val();
        projectJson.description = $("#project-description").val();

        $.ajax({
            url: baseUrl,
            type: "POST",
            data: JSON.stringify(projectJson),
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function () {
                getUserProjects();
                $("#project-details-modal").modal("hide");
            },
            error: function (data) {
                console.log(data);
            }
        });
    });
    
});