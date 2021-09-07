package pl.sukhina.sweater.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.sukhina.sweater.models.Role;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.repositories.UserRepository;
import pl.sukhina.sweater.services.MailSender;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Primary
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    final MailSender mailSender;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User createUser(User user) {
        var foundUser = userRepository.findUserByUsername(user.getUsername());
        if (foundUser != null) {
            return null;
        }
        user.setActive(true);
        user.setRoles(Collections.singletonList(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        if (!user.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to this app! Please, visit this link: http://localhost:8080/registration/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.sendMail(user.getEmail(), "Activation code", message);
        }

        return user;
    }

    @Override
    public User updateUser(User user, Long id) {
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(Long id) {
        var user = getUser(id);
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean activateUser(String code) {
        var foundUser = userRepository.findUserByActivationCode(code);
        if (foundUser == null) {
            return false;
        }
        foundUser.setActivationCode(null);
        userRepository.save(foundUser);
        return true;
    }
}
