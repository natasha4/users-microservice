package com.ng.users.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    protected Logger logger = Logger.getLogger(UsersController.class.getName());

    protected UserRepository userRepository;

    /**
     * Create an instance plugging in the repository of Users.
     *
     * @param userRepository
     *            An user repository implementation.
     */
    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;

        logger.info("UsersRepository says system has " + userRepository.countUsers() + " users");
    }

    /**
     * Fetch an user with the specified login.
     *
     * @param login
     *            9 characters.
     * @return The user if found.
     * @throws UserNotFoundException
     *             If the number is not recognised.
     */
    @RequestMapping("/users/{login}")
    public User byLogin(@PathVariable("login") String login) {

        logger.info("users-management-service byLogin() invoked: " + login);
        User user = userRepository.findByLogin(login);
        logger.info("users-management-service byLogin() found: " + user);

        if (user == null)
            throw new UserNotFoundException(login);
        else {
            return user;
        }
    }

    /**
     * Fetch users with the specified name. A partial case-insensitive match
     * is supported. So <code>http://.../users/name/a</code> will find any
     * users with upper or lower case 'a' in their name.
     *
     * @param partialName
     * @return A non-null, non-empty set of accounts.
     * @throws UserNotFoundException
     *             If there are no matches at all.
     */
    @RequestMapping("/users/search/{name}")
    public List<User> byName(@PathVariable("name") String partialName) {
        logger.info("users-management-service byName() invoked: "
                + userRepository.getClass().getName() + " for "
                + partialName);

        List<User> users = userRepository.findByNameContainingIgnoreCase(partialName);
        logger.info("users-management-service byName() found: " + users);

        if (users == null || users.size() == 0)
            throw new UserNotFoundException(partialName);
        else {
            return users;
        }
    }
}
