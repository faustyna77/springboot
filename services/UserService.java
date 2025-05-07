package zaklad.pogrzebowy.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zaklad.pogrzebowy.api.models.User;
import zaklad.pogrzebowy.api.repositories.OrderRepository;
import zaklad.pogrzebowy.api.repositories.TaskAssignmentRepository;
import zaklad.pogrzebowy.api.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCrypt;


@Service
public class UserService implements IUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private TaskAssignmentRepository assignmentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public User create(User user) {
        // Validate email uniqueness
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Get and validate password
        String rawPassword = user.getPasswordHash();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Hash the password
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return repository.save(user);
    }

    @Override
    @Transactional
    public User update(Long id, User updatedUser) {
        return repository.findById(id)
                .map(existingUser -> {
                    // Update basic info
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    existingUser.setRole(updatedUser.getRole());

                    // Handle email change with validation
                    if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
                        if (repository.findByEmail(updatedUser.getEmail()).isPresent()) {
                            throw new IllegalArgumentException("New email already exists");
                        }
                        existingUser.setEmail(updatedUser.getEmail());
                    }

                    // Handle password change if provided
                    if (updatedUser.getPasswordHash() != null &&
                            !updatedUser.getPasswordHash().isBlank() &&
                            !updatedUser.getPasswordHash().startsWith("$2a$")) {
                        existingUser.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
                    }

                    return repository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        // Usuń wszystkie przypisania i zamówienia tego użytkownika (jeśli takie istnieją)
        assignmentRepository.deleteAllByUserId(id);
        orderRepository.deleteAllByUserId(id);

        repository.deleteById(id);
    }
}