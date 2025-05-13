package com.suai.department43.loutsker.rpddrafter.web.auth;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Account;
import com.suai.department43.loutsker.rpddrafter.domain.payload.auth.LoginDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.auth.UserDTO;
import com.suai.department43.loutsker.rpddrafter.service.auth.external.AuthProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/authentication/")
public class AuthController {
    private final AuthProvider authProvider;

    public AuthController(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> authenticate(@RequestBody LoginDTO dto) {
        Authentication authentication = authProvider.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getLogin(),
                dto.getPassword()
        ));
        String token = authProvider.getTokenOnAuthenticatedUser(dto.getLogin());
        UserDTO user = new UserDTO();
        user.setToken(token);
        Account account = (Account) authentication.getPrincipal();
        user.setUser(account.getBearer());
        user.setRoles(account.getRoles());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
