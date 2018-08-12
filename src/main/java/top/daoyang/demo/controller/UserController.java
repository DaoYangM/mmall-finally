package top.daoyang.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.GenericException;
import top.daoyang.demo.exception.UserNotFoundException;
import top.daoyang.demo.exception.ValidationException;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.UserPasswordUpdateRequest;
import top.daoyang.demo.security.UserPrincipal;
import top.daoyang.demo.service.UserService;
import top.daoyang.demo.utils.RepxUtils;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthController authController;

    @GetMapping("/me")
    public ServerResponse me(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ServerResponse.createBySuccess(Optional.of(userService.findUserById(userPrincipal.getId()))
                .orElseThrow(() -> new UserNotFoundException(ExceptionEnum.USER_DOES_NOT_EXIST)));
    }

    @GetMapping("/exist/username/{username}")
    public ServerResponse checkUsername(@PathVariable String username) {
        if (StringUtils.hasText(username))
            if (userService.checkUsername(username)) {
                throw new GenericException(ExceptionEnum.USERNAME_EXISTED);
            } else {
                return ServerResponse.createBySuccess();
            }
        else
            return ServerResponse.createByErrorMessage("Username can't be empty");
    }


    @GetMapping("/exist/email/{email}")
    public ServerResponse checkEmail(@PathVariable String email) {
        if (StringUtils.hasText(email))
            if (RepxUtils.checkEmail(email))
                return ServerResponse.createBySuccess(userService.checkEmail(email));
            else
                throw new ValidationException(ExceptionEnum.VALIDATE_EMAIL);
        throw new GenericException(ExceptionEnum.EMPTY_EMAIL);
    }

    @GetMapping("/exist/phone/{phone}")
    public ServerResponse checkPhone(@PathVariable String phone) {
        if (StringUtils.hasText(phone) && RepxUtils.checkPhone(phone))
            if (userService.checkPhone(phone)) {
                throw new GenericException(ExceptionEnum.PHONE_EXISTED);
            } else {
                return ServerResponse.createBySuccess();
            }
        else
            return ServerResponse.createByErrorMessage("Phone number can't be empty or syntax error");
    }

    @PostMapping("/me/sms")
    public ServerResponse msSms(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        return authController.sendSms(userService.findUserById(userPrincipal.getId()).getPhone());
    }

    @PutMapping("/me/password")
    public ServerResponse updatePassword(@Valid @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return userService.updatePassword(userPasswordUpdateRequest, userService.findUserById(userPrincipal.getId())) ?
                ServerResponse.createBySuccessMessage("Updating password success"):
                ServerResponse.createByErrorMessage("Updating password failed");
    }
}
