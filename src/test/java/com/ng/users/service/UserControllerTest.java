package com.ng.users.service;

import com.ng.users.services.User;
import com.ng.users.services.UserNotFoundException;
import com.ng.users.services.UserRepository;
import com.ng.users.services.UsersController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by natalia on 23/11/17.
 */
public class UserControllerTest {

    private static String USER_LOGIN = "user12345";
    private static String USER_NAME = "The Test User";
    private static User TEST_USER = new User(USER_NAME, USER_LOGIN);

    private UsersController userController;

    private UserRepository usersRepositoryStub = new UserRepository() {
        @Override
        public User findByLogin(String login) {
            if (USER_LOGIN.equals(login)) {
                return TEST_USER;
            } else {
                throw new UserNotFoundException(login);
            }
        }

        @Override
        public List<User> findByNameContainingIgnoreCase(String partialName) {
            if (partialName!= null && USER_NAME.toLowerCase().startsWith(partialName.toLowerCase())) {
                return Collections.singletonList(TEST_USER);
            } else {
                throw new UserNotFoundException(partialName);
            }
        }

        @Override
        public int countUsers() {
            return 1;
        }
    };

    @Before
    public void setup() {
        userController = new UsersController(usersRepositoryStub);
    }

    @Test
    public void testByLoginValid() {
        User user = userController.byLogin(USER_LOGIN);

        Assert.assertNotNull(user);
        Assert.assertEquals(USER_LOGIN, user.getLogin());
        Assert.assertEquals(USER_NAME, user.getName());
    }

    @Test (expected = UserNotFoundException.class)
    public void testByLoginInvalid() {
        User user = userController.byLogin("user11111");

        Assert.fail("UserNotFoundException should be thrown");
    }

    @Test
    public void testByName1() throws Exception {
        List<User> users = userController.byName(USER_NAME.substring(0, 3).toUpperCase());

        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());
        Assert.assertNotNull(users.get(0));
        Assert.assertEquals(USER_LOGIN, users.get(0).getLogin());
        Assert.assertEquals(USER_NAME, users.get(0).getName());
    }

    @Test
    public void testByName2() throws Exception {
        List<User> users = userController.byName(USER_NAME);

        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());
        Assert.assertNotNull(users.get(0));
        Assert.assertEquals(USER_LOGIN, users.get(0).getLogin());
        Assert.assertEquals(USER_NAME, users.get(0).getName());
    }

    @Test (expected = UserNotFoundException.class)
    public void testByNameInvalid1() {
        List<User> user = userController.byName("*");

        Assert.fail("UserNotFoundException should be thrown");
    }

}
