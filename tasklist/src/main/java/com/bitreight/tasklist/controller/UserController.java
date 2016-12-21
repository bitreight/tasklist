package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.dto.ChangeUserPasswordDto;
import com.bitreight.tasklist.dto.UserProfileDto;
import com.bitreight.tasklist.security.CustomUserDetails;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.exception.ServiceInvalidUserPasswordException;
import com.bitreight.tasklist.service.exception.ServiceProfileImageUploadException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ResponseEntity<UserProfileDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails user)
            throws ServiceUserNotFoundException {

        UserProfileDto userProfileDto = userService.getById(user.getId());
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeUserInfo(@AuthenticationPrincipal CustomUserDetails user,
                                               @Valid @RequestBody UserProfileDto userProfileDto)
            throws ServiceUserNotFoundException {

        userProfileDto.setId(user.getId());
        userService.update(userProfileDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "password", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeUserPassword(@AuthenticationPrincipal CustomUserDetails user,
                                                   @RequestBody @Valid ChangeUserPasswordDto passwordDto)
            throws ServiceUserNotFoundException, ServiceInvalidUserPasswordException {

        passwordDto.setId(user.getId());
        userService.changePassword(passwordDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "image", method = RequestMethod.POST)
    public ResponseEntity<String> uploadProfileImage(@AuthenticationPrincipal CustomUserDetails user,
                                                     @RequestParam("profileImage") MultipartFile profileImage)
            throws ServiceProfileImageUploadException, ServiceUserNotFoundException, IOException {

        String profileImagePath = userService.setProfileImage(profileImage.getBytes(), user.getId());
        return new ResponseEntity<>(profileImagePath, HttpStatus.OK);
    }
}