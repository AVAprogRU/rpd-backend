package com.suai.department43.loutsker.rpddrafter.service.business.internal;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Account;
import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Role;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {
    private final PasswordGenerator passwordGenerator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DBProvider provider;

    public RegistrationService(BCryptPasswordEncoder passwordEncoder,
                               DBProvider provider,
                               PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
        this.passwordEncoder = passwordEncoder;
        this.provider = provider;
    }

    public String registerTeacher(Teacher teacher) {
        String login = getLoginFromEmail(teacher.getEmail());
        String rawPassword = generatePassword();
        System.out.println(rawPassword);
        String passwordHashed = passwordEncoder.encode(rawPassword);
        Account account = new Account();
        account.setBearer(teacher);
        account.setLogin(login);
        account.setPassword(passwordHashed);
        Role teacherRole = provider.getRoleByName("TEACHER");
        account.setRoles(List.of(teacherRole));
        provider.saveAccount(account);
        return "Your login: " + login + "; password: " + rawPassword + "\n";
    }

    private String getLoginFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            return email;
        }
    }

    private String generatePassword() {
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(3);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(3);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(3);

        return passwordGenerator.generatePassword(9, lowerCaseRule, upperCaseRule, digitRule);
    }
}
