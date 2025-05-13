package com.suai.department43.loutsker.rpddrafter.service.auth.internal;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Account;
import com.suai.department43.loutsker.rpddrafter.repository.auth.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JDBCUserService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public JDBCUserService(AccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findAccountByLogin(username);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        return account.get();
    }

    public boolean passwordHashMatches(String rawPassword, String passwordHashOnLogin) {
        return passwordEncoder.matches(rawPassword, passwordHashOnLogin);
    }
}
