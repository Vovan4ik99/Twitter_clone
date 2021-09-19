package pl.sukhina.sweater.services.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sukhina.sweater.models.Role;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.repositories.UserRepository;
import pl.sukhina.sweater.services.MailSender;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    final MailSender mailSender;

    final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        sendActivationCode(user);

        return user;
    }

    private void sendActivationCode(User user) {
        if (!user.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to this app! Please, visit this link: http://localhost:8080/registration/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.sendMail(user.getEmail(), "Activation code", message);
        }
    }

    public User updateUser(User user, String username, Map<String, String> form) {
        user.setId(user.getId());
        user.setUsername(username);
        List<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toList());
        user.getRoles().clear();
        for (String key: form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        if (email != null && !email.isEmpty() && !email.equals(userEmail) || (userEmail != null && !userEmail.equals(email))) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            sendActivationCode(user);
        }
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
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
