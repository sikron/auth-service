package com.skronawi.authservice.service.service;

import com.skronawi.authservice.service.bl.NotExistingException;
import com.skronawi.authservice.service.bl.UserService;
import com.skronawi.tokenservice.jwt.api.UserInfo;
import com.skronawi.tokenservice.jwt.api.UserInfoImpl;

import java.util.Arrays;

public class EverybodyIsACrawlerUserService implements UserService {

    public UserInfo getByUsernameAndPassword(String username, String password) throws NotExistingException {
        UserInfo user = new UserInfoImpl();
        user.setUsername(username);
        user.setRoles(Arrays.asList("crawler"));
        return user;
    }
}
