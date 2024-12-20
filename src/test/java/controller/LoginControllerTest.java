package test.java.controller;

import main.java.controller.LoginController;
import main.java.model.AuthenticationModel;
import main.java.utils.AuthResult;
import main.java.users.User;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class LoginControllerTest {

    private LoginController loginController;
    private AuthenticationModel authModel;
    private JFrame loginFrame;

    @Before
    public void setUp() {
        // Using real AuthenticationModel and JFrame objects.
        authModel = new AuthenticationModel(null); // We are not testing the database connection here.
        loginController = new LoginController(authModel);
        loginFrame = new JFrame();
    }

    @Test
    public void testHandleLogin_emptyFields() {
        // Test 1: When the username or password is empty
        loginController.handleLogin("", "password", loginFrame);
        assertTrue(loginFrame.isDisplayable());

        loginController.handleLogin("username", "", loginFrame);
        assertTrue(loginFrame.isDisplayable());
    }

    @Test
    public void testHandleLogin_invalidCredentials() {
        // Test 2: Login with invalid credentials
        authModel = new AuthenticationModel(null) {
            @Override
            public AuthResult authenticateUser(String username, String password) {
                return null; // Simulating an invalid user.
            }
        };
        loginController = new LoginController(authModel);

        loginController.handleLogin("invalidUser", "invalidPass", loginFrame);
        assertTrue(loginFrame.isDisplayable());
    }

    @Test
    public void testHandleLogin_validCustomerLogin() {
        // Test 3: Valid customer login
        authModel = new AuthenticationModel(null) {
            @Override
            public AuthResult authenticateUser(String username, String password) {
                if (username.equals("customerUser") && password.equals("customerPass")) {
                    return new AuthResult("customer", new User(1, "customerUser"));
                }
                return null;
            }
        };
        loginController = new LoginController(authModel);

        loginController.handleLogin("customerUser", "customerPass", loginFrame);
        assertFalse(loginFrame.isDisplayable());
    }

    @Test
    public void testHandleLogin_validMechanicLogin() {
        // Test 4: Valid mechanic login
        authModel = new AuthenticationModel(null) {
            @Override
            public AuthResult authenticateUser(String username, String password) {
                if (username.equals("mechanicUser") && password.equals("mechanicPass")) {
                    return new AuthResult("mechanic", new User(2, "mechanicUser"));
                }
                return null;
            }
        };
        loginController = new LoginController(authModel);

        loginController.handleLogin("mechanicUser", "mechanicPass", loginFrame);
        assertFalse(loginFrame.isDisplayable());
    }

    @Test
    public void testHandleLogin_validManagerLogin() {
        // Test 5: Valid manager login
        authModel = new AuthenticationModel(null) {
            @Override
            public AuthResult authenticateUser(String username, String password) {
                if (username.equals("managerUser") && password.equals("managerPass")) {
                    return new AuthResult("manager", new User(3, "managerUser"));
                }
                return null;
            }
        };
        loginController = new LoginController(authModel);

        loginController.handleLogin("managerUser", "managerPass", loginFrame);
        assertFalse(loginFrame.isDisplayable());
    }
}
