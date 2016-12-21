$(document).ready(function () {

    getUserProfile();
    prepareImageCloudinary();
    
});

$("#edit-user-info").click(function () {
    getUserInfo();
});

$("#save-user-info").click(function () {
    $(".user-error").hide();
    $(".user-error").text("");

    var userJson = JSON.parse(JSON.stringify(userInfoJson));
    userJson.name = $("#user-name-input").val();
    userJson.surname = $("#user-surname-input").val();
    updateUserInfo(userJson);
});

$("#change-password").click(function () {
    $("#password-modal").modal("show");
});

$("#save-password").click(function (e) {
    $(".password-error").hide();

    var passJson = JSON.parse(JSON.stringify(passwordJson));
    var newPass = $("#new-password-input").val();
    var confirmNewPass = $("#password-confirm-input").val();

    if(confirmPassword(newPass, confirmNewPass)) {
        passJson.oldPassword = $("#password-input").val();
        passJson.newPassword = newPass;
        changePassword(passJson);
    } else {
        $("#password-confirm-error").text("Passwords are not match.");
        $("#password-confirm-error").show();
    }

    e.preventDefault();
});

$("#upload-image").click(function () {
    $("#default-upload").trigger("click");
});

var apiBaseUrl = "/tasklist/api/user";
var emptyUserImage = "/tasklist/resources/img/user.png";

var userInfoJson = { "name": null, "surname": null };
var passwordJson = { "oldPassword": null, "newPassword": null };

function uploadProfileImage() {
    var fileFormData = new FormData();
    fileFormData.append("profileImage", $("#default-upload")[0].files[0]);

    $.ajax({
        type: "POST",
        url: apiBaseUrl + "/image",
        data: fileFormData,
        contentType: false,
        processData: false,
        cache: false,
        success: function (imageUrl) {
            var preparedUrl = prepareImageCloudinary(imageUrl);
            $("#profile-image").attr("src", preparedUrl);
        },
        error: function (e) {
            //common error
        }
    });
}

function getUserProfile() {
    $.ajax({
        type: "GET",
        url: apiBaseUrl + "/profile",
        success: function (user) {
            var name = $("#name");
            var surname = $("#surname");

            $("#username").text(user.username);

            (user.name == null || user.name == "") ?
                name.text("Not specified") :
                name.text(user.name);
            (user.surname == null || user.surname == "") ?
                surname.text("Not specified") :
                surname.text(user.surname);

            var profileImage = user.profileImagePath;
            if(profileImage == null || profileImage == "") {
                $("#profile-image").attr("src", emptyUserImage);

            } else {
                var preparedUrl = prepareImageCloudinary(profileImage);
                $("#profile-image").attr("src", preparedUrl);
            }
        },
        error: function (errorResponse) {
            //common error
        }
    });
}

function getUserInfo() {
    $.ajax({
        type: "GET",
        url: apiBaseUrl + "/profile",
        success: function (user) {
            $("#user-name-input").val(user.name);
            $("#user-surname-input").val(user.surname);
            $("#user-info-modal").modal("show");
        },
        error: function (errorResponse) {
            //common error
        }
    });
}

function updateUserInfo(userInfoJson) {
    $.ajax({
        url: apiBaseUrl + "/profile",
        type: "PUT",
        data: JSON.stringify(userInfoJson),
        contentType: "application/json",
        success: function () {
            $("#user-info-modal").modal("hide");
            getUserProfile();
        },
        error: function (errorResponse) {
            var errorMessage = errorResponse.responseJSON.message;
            var fieldErrors = errorResponse.responseJSON.fieldErrors;

            if(errorMessage) {
                showUserSaveError(errorMessage);
            }
            if(fieldErrors) {
                showUserFieldErrors(fieldErrors);
            }
        }
    });
}

function changePassword(passwordJson) {
    $.ajax({
        url: apiBaseUrl + "/password",
        type: "PUT",
        data: JSON.stringify(passwordJson),
        contentType: "application/json",
        success: function () {
            $("#password-modal").modal("hide");
        },
        error: function (errorResponse) {
            var errorMessage = errorResponse.responseJSON.message;
            var fieldErrors = errorResponse.responseJSON.fieldErrors;

            if(errorMessage) {
                showSavePasswordError(errorMessage);
            }
            if(fieldErrors) {
                showPasswordFieldErrors(fieldErrors);
            }

            clearPasswordForm();
        }
    });
}

function showUserSaveError(message) {
    var errorDiv = $("#user-save-error");
    errorDiv.text(message);
    errorDiv.show();
}

function showUserFieldErrors(errors) {
    var nameError = errors.name;
    var surnameError = errors.surname;
    var errorDiv;

    if(nameError) {
        errorDiv = $("#name-error");
        errorDiv.text(nameError);
        errorDiv.show();
    }
    if(surnameError) {
        errorDiv = $("#surname-error");
        errorDiv.text(surnameError);
        errorDiv.show();
    }
}

function showSavePasswordError(message) {
    var errorDiv = $("#password-change-error");
    errorDiv.text(message);
    errorDiv.show();
}

function showPasswordFieldErrors(errors) {
    var newPasswordError = errors.newPassword;
    if(newPasswordError) {
        $("#newpassword-error").text(newPasswordError);
        $("#newpassword-error").show();
    }
}

$("#user-info-modal").on("hide.bs.modal", function () {
   $(".user-error").hide();
   $(".user-error").text("");
});

$("#password-modal").on("hide.bs.modal", function () {
   $(".password-error").hide();
   $(".password-error").text("");
    clearPasswordForm();
});

function clearPasswordForm() {
    $("#password-form")[0].reset();
}

function confirmPassword() {
    var newPassword = $("#new-password-input").val();
    var confirmPassword = $("#password-confirm-input").val();
    return newPassword == confirmPassword;
}

function prepareImageCloudinary(url) {
    var modification = "/c_fill,h_300,w_300";
    var splitPos = url.indexOf("/upload") + "/upload".length;
    var prefix = url.substr(0, splitPos);
    var suffix = url.substr(splitPos);
    return "".concat(prefix, modification, suffix);
}