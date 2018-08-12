package top.daoyang.demo.service;

import top.daoyang.demo.payload.request.RegisterRequest;

public interface AuthService {

    boolean sendSms(String phone);

    boolean register(RegisterRequest registerRequest);
}
