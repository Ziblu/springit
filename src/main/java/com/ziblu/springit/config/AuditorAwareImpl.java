package com.ziblu.springit.config;

import com.ziblu.springit.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        if(SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return Optional.of("master@gmail.com");
        } else {
            return Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        }
    }
}
