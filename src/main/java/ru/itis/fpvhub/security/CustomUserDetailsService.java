package ru.itis.fpvhub.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.repository.UserRepository;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findForAuthentication(usernameOrEmail)
                .map(user -> {
                    List<GrantedAuthority> authorities = user.getRoles()
                            .stream()
                            .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.getName().name()))
                            .toList();
                    return CustomUserPrincipal.from(user, authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
    }
}
