
package zaklad.pogrzebowy.api.services;


import zaklad.pogrzebowy.api.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User create(User user);
    User update(Long id, User user);
    void delete(Long id);
}
