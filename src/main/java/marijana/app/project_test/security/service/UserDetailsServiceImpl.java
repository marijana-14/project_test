package marijana.app.project_test.security.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import marijana.app.project_test.model.User;
import marijana.app.project_test.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            Set<GrantedAuthority> authorities = new HashSet<>();
            user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                    .forEach(authorities::add);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    authorities);
        }
        else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}