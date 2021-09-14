package pl.sukhina.sweater.services.user;

import pl.sukhina.sweater.models.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> getUsers();

    User getUser(Long id);

    User createUser(User user);

    User deleteUser(Long id);

    User findUserByUsername(String username);

    boolean activateUser(String code);

    User updateUser(User user, String username, Map<String, String> form);

    User updateUserProfile(User user, String password, String email);
}
