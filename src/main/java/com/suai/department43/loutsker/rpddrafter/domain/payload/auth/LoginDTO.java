package com.suai.department43.loutsker.rpddrafter.domain.payload.auth;

public class LoginDTO {
    private String login;
    private String password;

    public LoginDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
