package com.skronawi.authservice.service.communication;

import com.skronawi.authservice.service.bl.UserService;
import com.skronawi.tokenservice.jwt.api.Token;
import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenValidity;
import com.skronawi.tokenservice.jwt.api.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/token")
public class TokenBasicResource {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public TokenBasicResource(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(path = "/basic", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> getForCredentials(@RequestHeader(value = "Authorization") String auth) {

        //TODO let this do a credentialsAnalyser by REST path
        String[] usernamePassword = assertBasicAuthValue(auth);

        UserInfo user = userService.getByUsernameAndPassword(usernamePassword[0], usernamePassword[1]);

        String tokenString = tokenService.buildToken(user);

        return new ResponseEntity<>(new Token(tokenString), HttpStatus.OK);
    }

    @RequestMapping(path = "/validate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenValidity> validate(@RequestParam("token") String token) {

        TokenValidity tokenValidity = tokenService.validateToken(token);

        return new ResponseEntity<>(tokenValidity, HttpStatus.OK);
    }

    private String[] assertBasicAuthValue(String auth) {
        if (!auth.startsWith("Basic ")) {
            throw new IllegalArgumentException("Authorization must start with 'BASIC '");
        }
        String usernamePasswordInBase64 = auth.substring(6, auth.length());
        String usernamePassword;
        try {
            usernamePassword = new String(Base64Utils.decodeFromString(usernamePasswordInBase64));
        } catch (Exception e) {
            throw new IllegalArgumentException("problem with decoding the base64 username-password", e);
        }
        if (usernamePassword.indexOf(":") != usernamePassword.lastIndexOf(":")) {
            throw new IllegalArgumentException("problem with colon");
        }

        return usernamePassword.split(":");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void iae() {
        // just convert a predefined exception to an HTTP status code
        //TODO FIXME this is no JSON then!
    }
}
