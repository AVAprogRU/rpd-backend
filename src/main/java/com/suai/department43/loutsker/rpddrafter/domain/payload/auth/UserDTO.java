package com.suai.department43.loutsker.rpddrafter.domain.payload.auth;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Role;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Person;

import java.util.List;

public class UserDTO {
    private String token;
    private Person user;
    private List<Role> roles;

    public UserDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
