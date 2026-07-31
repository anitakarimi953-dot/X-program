package client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class LoginFrame extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    private final JButton loginButton;
    private final JButton registerButton;

    private final Client client;

    public LoginFrame() {

        client = new Client("localhost", 5000);

        setTitle("X-Program");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel usernameLabel =
                new JLabel("Username:");

        JLabel passwordLabel =
                new JLabel("Password:");

        usernameField =
                new JTextField();

        passwordField =
                new JPasswordField();

        loginButton =
                new JButton("Login");

        registerButton =
                new JButton("Register");

        add(usernameLabel);
        add(usernameField);

        add(passwordLabel);
        add(passwordField);

        add(loginButton);
        add(registerButton);

        add(new JLabel());
        add(new JLabel());

        loginButton.addActionListener(
                e -> login()
        );

        registerButton.addActionListener(
                e -> register()
        );
    }

    private void register() {

        String username =
                usernameField.getText();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username and password are required."
            );

            return;
        }

        String response =
                client.register(
                        username,
                        password
                );

        if ("REGISTER_SUCCESS".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful!"
            );

        } else if ("INVALID_PASSWORD".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 8 characters,\n"
                            + "one uppercase letter,\n"
                            + "one lowercase letter,\n"
                            + "and one number."
            );

        } else if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed.\n"
                            + "Username may already exist."
            );
        }
    }

    private void login() {

        String username =
                usernameField.getText();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username and password are required."
            );

            return;
        }

        String response =
                client.login(
                        username,
                        password
                );

        if (response != null
                && response.startsWith(
                "LOGIN_SUCCESS|"
        )) {

            String sessionId =
                    response.substring(
                            "LOGIN_SUCCESS|".length()
                    );

            DashboardFrame dashboard =
                    new DashboardFrame(sessionId);

            dashboard.setVisible(true);

            dispose();

        } else if ("CONNECTION_ERROR"
                .equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Login failed."
            );
        }
    }
}