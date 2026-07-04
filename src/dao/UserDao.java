package dao;

import model.User;
import java.util.List;

public interface UserDao {
    boolean addUser(User user);
    User getUserById(String userId);
    User getUserByEmail(String email);
    User getUserByLibraryId(String libraryId);
    boolean updateUser(User user);
    boolean deleteUser(String userId);
    List<User> findAll();
    List<User> findByRole(String role);
    List<User> findActive();
    int count();
}
