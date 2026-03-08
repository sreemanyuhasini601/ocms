package com.reimbursement.services;

import com.reimbursement.DAOs.UserDAO;
import com.reimbursement.models.User;
import com.reimbursement.models.DTOs.LoginUserDTO;
import com.reimbursement.models.DTOs.RegistrationUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(RegistrationUserDTO registerUserDTO) throws IllegalArgumentException {
        if (registerUserDTO.getUsername() == null || registerUserDTO.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (registerUserDTO.getPassword() == null || registerUserDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Set default role if not provided
        String role = registerUserDTO.getRole() != null ? registerUserDTO.getRole() : "employee";

        User newUser = new User();
        newUser.setUsername(registerUserDTO.getUsername());
        newUser.setPassword(registerUserDTO.getPassword()); // Consider hashing the password before storing
        newUser.setFirstName(registerUserDTO.getFirstName());
        newUser.setLastName(registerUserDTO.getLastName());
        newUser.setRole(role);
        newUser.setEmail(registerUserDTO.getEmail()); // Set email from RegistrationUserDTO
        return userDAO.save(newUser);
    }

    public Optional<User> loginUser(LoginUserDTO loginUserDTO) throws IllegalArgumentException {
        if (loginUserDTO.getUsername() == null || loginUserDTO.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (loginUserDTO.getPassword() == null || loginUserDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        return userDAO.findByUsernameAndPassword(loginUserDTO.getUsername(), loginUserDTO.getPassword());
    }

    public void deleteUser(int userId) {
        Optional<User> user = userDAO.findById(userId);
        if (user.isPresent()) {
            userDAO.delete(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + userId + " does not exist");
        }
    }

    public User findByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        return user.orElse(null);
    }
}
