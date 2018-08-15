package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.ValidationException;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.reponse.JwtAuthenticationReponse;
import top.daoyang.demo.payload.request.JwtAuthenticationRequest;
import top.daoyang.demo.payload.request.RegisterRequest;
import top.daoyang.demo.security.JwtTokenProvider;
import top.daoyang.demo.service.AuthService;
import top.daoyang.demo.util.RepxUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserController userController;

    @PostMapping("/login")
    public JwtAuthenticationReponse login(@Valid @RequestBody JwtAuthenticationRequest jwtAuthenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtAuthenticationRequest.getUsername(),
                jwtAuthenticationRequest.getPassword()
        ));

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return new JwtAuthenticationReponse(jwtToken);
    }

    @PostMapping("/register")
    public ServerResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userController.checkUsername(registerRequest.getUsername()).isSuccess() && userController.checkPhone(registerRequest.getPhone()).isSuccess()) {
            return authService.register(registerRequest) ? ServerResponse.createBySuccess() :
                    ServerResponse.createByErrorMessage("User register failed");
        }

        return ServerResponse.createByErrorMessage("User register check username or phone failed");
    }

    @PostMapping("/sms")
    public ServerResponse sendSms(@RequestBody String phone) {
        //TODO check ip;
        if (RepxUtils.checkPhone(phone)) {
                return authService.sendSms(phone) ? ServerResponse.createBySuccess() :
                        ServerResponse.createByErrorMessage("Sending sms smsCode failed");
            }
        throw new ValidationException(ExceptionEnum.VALIDATE_PHONE);
    }
}
