package com.mvrcm.login_jwt_system;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -612312398046844L;
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}
