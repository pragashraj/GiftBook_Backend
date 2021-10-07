package com.giftbook.giftBook.auth;

import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserAuthenticationRepository authRepository;

    @Autowired
    public UserDetailsServiceImpl(UserAuthenticationRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthentication auth = authRepository.findByEmail(email);
        if (auth == null) {
            log.error("User not found for email: {}", email);
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User found for email: {} and with id : {}", email, auth.getId());
        return new UserDetailsImpl(
                auth.getId(),
                auth.getEmail(),
                auth.getPassword()
        );
    }
}
