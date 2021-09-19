package pl.sukhina.sweater.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.user.UserService;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SecurityUserService implements UserDetailsService {

    final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not Found");
        }
        return user;
    }
}
