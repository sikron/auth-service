package com.skronawi.authservice.service.bl;

import com.skronawi.tokenservice.jwt.api.UserInfo;

public interface UserService {

    UserInfo getByUsernameAndPassword(String username, String password) throws NotExistingException;
}
