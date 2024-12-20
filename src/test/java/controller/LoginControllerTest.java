package test.java.controller;

import main.java.controller.LoginController;
import main.java.model.AuthenticationModel;
import main.java.model.DatabaseConnection;
import main.java.utils.AuthResult;
import main.java.users.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private LoginController loginController;
    private AuthenticationModel mockAuthModel;
    private JFrame mockLoginFrame;

    @Before
    public void setUp() {
        mockAuthModel = mock(AuthenticationModel.class);
        loginController = new LoginController(mockAuthModel);
        mockLoginFrame = mock(JFrame.class);
    }

    @Test
    public void testHandleLogin() {
        // Test 1: Empty username or password
        loginController.handleLogin("", "password", mockLoginFrame);
        verify(mockLoginFrame, times(1)).dispose();

        loginController.handleLogin("username", "", mockLoginFrame);
        verify(mockLoginFrame, times(2)).dispose();

        // Test 2: Invalid credentials
        when(mockAuthModel.authenticateUser("invalidUser", "invalidPass"))
                .thenReturn(null);

        loginController.handleLogin("invalidUser", "invalidPass", mockLoginFrame);
        verify(mockLoginFrame, times(3)).dispose();

        // Test 3: Valid customer login
        User mockCustomer = mock(User.class);
        AuthResult mockCustomerResult = new AuthResult("customer", mockCustomer);

        when(mockAuthModel.authenticateUser("customerUser", "customerPass"))
                .thenReturn(mockCustomerResult);

        loginController.handleLogin("customerUser", "customerPass", mockLoginFrame);
        verify(mockLoginFrame, times(4)).dispose();

        // Test 4: Valid mechanic login
        User mockMechanic = mock(User.class);
        AuthResult mockMechanicResult = new AuthResult("mechanic", mockMechanic);

        when(mockAuthModel.authenticateUser("mechanicUser", "mechanicPass"))
                .thenReturn(mockMechanicResult);

        loginController.handleLogin("mechanicUser", "mechanicPass", mockLoginFrame);
        verify(mockLoginFrame, times(5)).dispose();

        // Test 5: Valid manager login
        User mockManager = mock(User.class);
        AuthResult mockManagerResult = new AuthResult("manager", mockManager);

        when(mockAuthModel.authenticateUser("managerUser", "managerPass"))
                .thenReturn(mockManagerResult);

        loginController.handleLogin("managerUser", "managerPass", mockLoginFrame);
        verify(mockLoginFrame, times(6)).dispose();
    }
}
