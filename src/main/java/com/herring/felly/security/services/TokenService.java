package com.herring.felly.security.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements UserDetailsService {

    @Value("${bearer}")
    private String bearer;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {

        if (token.equals(bearer)) {
            return new User(token, "",
                    AuthorityUtils
                            .commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
        } else {
            throw new UsernameNotFoundException("Token not found: " + token);
        }
    }
}
