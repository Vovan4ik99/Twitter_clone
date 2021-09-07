package pl.sukhina.sweater.services.user;

import pl.sukhina.sweater.models.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user, Long id);

    User deleteUser(Long id);

    User findUserByUsername(String username);

    boolean activateUser(String code);
}
