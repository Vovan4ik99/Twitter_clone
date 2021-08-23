package pl.sukhina.sweater.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.repositories.UserRepository;
import java.util.List;

@Primary
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

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
        return userRepository.save(user);
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
}
