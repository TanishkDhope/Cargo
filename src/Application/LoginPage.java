package Application;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("Login Page");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Use GridBagLayout for flexible sizing

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Add left panel (4 parts)
        gbc.weightx = 0.4; // 4 parts
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLoginPanel(), gbc);

        // Add right panel (6 parts)
        gbc.weightx = 0.6; // 6 parts
        gbc.gridx = 1;
        add(createSignupPanel(), gbc);

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(60, 179, 113)); // Grass green color
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setBounds(90, 100, 300, 40); // Moved further left
        panel.add(welcomeLabel);

        JLabel impactfulLine = new JLabel(
                "<html>Let's start your journey with us.<br>Experience the best service ever!</html>");
        impactfulLine.setForeground(Color.WHITE);
        impactfulLine.setFont(new Font("Arial", Font.ITALIC, 18));
        impactfulLine.setBounds(20, 160, 300, 60); // Moved further left
        panel.add(impactfulLine);

        JButton signInButton = createRoundedButton("SIGN UP", new Color(60, 179, 113), Color.WHITE);
        signInButton.setBounds(60, 270, 150, 40); // Adjusted position
        panel.add(signInButton);

        signInButton.addActionListener(e -> goToDetailsPage());

        return panel;
    }

    private JPanel createSignupPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);

        JLabel createAccountLabel = new JLabel("Welcome Back!");
        createAccountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        createAccountLabel.setForeground(new Color(60, 179, 113));
        createAccountLabel.setBounds(100, 50, 200, 30);
        panel.add(createAccountLabel);

        JTextField nameField = createStyledTextField("Name");
        nameField.setBounds(100, 120, 200, 40);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                nameField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(nameField);

        JTextField emailField = createStyledTextField("Email");
        emailField.setBounds(100, 180, 200, 40);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                emailField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(emailField);

        JPasswordField passwordField = createStyledPasswordField("Password");
        passwordField.setBounds(100, 240, 200, 40);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(passwordField);

        JButton logInButton = createRoundedButton("LOG IN", new Color(60, 179, 113), Color.WHITE);
        logInButton.setBounds(100, 300, 200, 40);
        panel.add(logInButton);

        logInButton.addActionListener(e -> handleLogin(emailField, passwordField));

        return panel;
    }

    private void handleLogin(JTextField emailField, JPasswordField passwordField) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in both email and password.");
            return;
        }
        if ("admin123@gmail.com".equals(email) && "admin123".equals(password)) {
            // Open Admin Dashboard
            
            JOptionPane.showMessageDialog(null, "Login successful! Welcome Admin!");
            this.dispose(); // Close login page
            AdminDashboard.main(null); // Navigate to Admin Dashboard
           
            return; // Exit the method after redirecting to the admin dashboard
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root",
                "Dadu2468");
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {

            // Prepare the query to find the user with the provided email
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    // User found, now check the password
                    String storedPassword = rs.getString("password_hash"); // Retrieve the stored password
                    System.out.println("Stored Password: " + storedPassword); // Debugging

                    if (password.equals(storedPassword)) {
                        // Password is correct
                        String userName = rs.getString("name");
                        JOptionPane.showMessageDialog(null, "Login successful! Welcome " + userName + "!");
                        dispose();    // Close login page
                        LandingPage.setUserName(userName); //pass username as argument
                        LandingPage.main(null); //Navigate to Landing Page
                     
                    } else {
                        // Incorrect password
                        JOptionPane.showMessageDialog(null, "Invalid email or password.");
                    }
                } else {
                    // No user found with the provided email
                    JOptionPane.showMessageDialog(null, "Invalid email or password.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Login failed: " + e.getMessage());
        }
    }

    private void goToDetailsPage() {
        new DetailsPage(this); // Pass reference to LoginPage
        dispose(); // Close login page
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(60, 179, 113), 2)); // Keep border

        // Set placeholder text
        textField.setForeground(Color.GRAY); // Color for placeholder text
        textField.setText(placeholder);

        // Add focus listener to manage placeholder visibility
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });

        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(60, 179, 113), 2)); // Keep border

        // Set placeholder text
        passwordField.setForeground(Color.GRAY);
        passwordField.setText(placeholder);

        // Add focus listener to manage placeholder visibility
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                }
            }
        });

        return passwordField;
    }

    private JButton createRoundedButton(String text, Color bgColor, Color borderColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }

            @Override
            public void setBorderPainted(boolean b) {
                super.setBorderPainted(false);
            }
        };
        button.setOpaque(false);
        button.setForeground(borderColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 139, 34)); // Dark green
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Arial", Font.BOLD, 18)); // Slightly increase font size
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(borderColor);
                button.setFont(new Font("Arial", Font.BOLD, 16)); // Reset font size
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}

class HomePage extends JFrame {
    public HomePage(String userName, LoginPage loginPage) {
        setTitle("Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Set explicit BorderLayout

        JLabel welcomeLabel = new JLabel("Welcome, " + userName + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER); // Add to the center

        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            new LoginPage();
            dispose();
        });
        add(backButton, BorderLayout.SOUTH); // Add the button at the bottom

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class DetailsPage extends JFrame {
    public DetailsPage(LoginPage loginPage) {
        setTitle("Create New Account");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Use GridBagLayout for flexible sizing

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Add left panel (6 parts)
        gbc.weightx = 0.6; // 6 parts
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createDetailsPanel(), gbc);

        // Add right panel (4 parts)
        gbc.weightx = 0.4; // 4 parts
        gbc.gridx = 1;
        add(createWelcomePanel(), gbc);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel instructionsLabel = new JLabel("Create New Account");
        instructionsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        instructionsLabel.setForeground(new Color(60, 179, 113)); // Set to green
        instructionsLabel.setBounds(100, 20, 250, 30); // Moved further left
        panel.add(instructionsLabel);

        JTextField nameField = createStyledTextField("Name");
        nameField.setBounds(100, 80, 250, 40); // Moved further left
        nameField.setBorder(BorderFactory.createCompoundBorder(
                nameField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(nameField);

        JTextField addressField = createStyledTextField("Address");
        addressField.setBounds(100, 140, 250, 40); // Moved further left
        addressField.setBorder(BorderFactory.createCompoundBorder(
                addressField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(addressField);

        JTextField ageField = createStyledTextField("Age");
        ageField.setBounds(100, 200, 250, 40); // Moved further left
        ageField.setBorder(BorderFactory.createCompoundBorder(
                ageField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(ageField);

        JTextField emailField = createStyledTextField("Email");
        emailField.setBounds(100, 260, 250, 40); // Moved further left
        emailField.setBorder(BorderFactory.createCompoundBorder(
                emailField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(emailField);

        JPasswordField passwordField = createStyledPasswordField("Password");
        passwordField.setBounds(100, 320, 250, 40); // Moved further left
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // top, left, bottom, right padding
        panel.add(passwordField);

        JButton submitButton = createRoundedButton("SUBMIT", new Color(60, 179, 113), Color.WHITE);
        submitButton.setBounds(100, 380, 250, 40); // Moved further left
        panel.add(submitButton);

        submitButton.addActionListener(e -> {
            if (handleSignUp(nameField, emailField, passwordField, ageField, addressField)) {
                JOptionPane.showMessageDialog(this, "Registered Successfully!");
                dispose(); // Close the registration page
            }
        });

        return panel;
    }

    private boolean handleSignUp(JTextField nameField, JTextField emailField, JPasswordField passwordField,
            JTextField ageField, JTextField addressField) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String address = addressField.getText().trim();
        String age = ageField.getText().trim();

        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || age.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return false;
        }

        // Insert the user into the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root",
                "Dadu2468");
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO users (name, email, password_hash, address, age) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password); // Directly inserting the plain password
            stmt.setString(4, address);
            stmt.setString(5, age);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Returns true if the user was inserted successfully

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage());
            return false; // Return false if there was an error
        }
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(60, 179, 113));
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Already a User?");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBounds(75, 100, 200, 30); // Moved further left
        panel.add(welcomeLabel);

        JButton LoginButton = createRoundedButton("Login", new Color(60, 179, 113), Color.WHITE);
        LoginButton.setBounds(40, 200, 250, 40); // Moved further left
        panel.add(LoginButton);
        LoginButton.addActionListener(e -> {
            new LoginPage(); // Pass reference to LoginPage
            dispose(); // close DetailsPage

        });

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(60, 179, 113), 2)); // Keep border

        // Set placeholder text
        textField.setForeground(Color.GRAY); // Color for placeholder text
        textField.setText(placeholder);

        // Add focus listener to manage placeholder visibility
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });

        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(60, 179, 113), 2)); // Keep border

        // Set placeholder text
        passwordField.setForeground(Color.GRAY);
        passwordField.setText(placeholder);

        // Add focus listener to manage placeholder visibility
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                }
            }
        });

        return passwordField;
    }

    private JButton createRoundedButton(String text, Color bgColor, Color borderColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }

            @Override
            public void setBorderPainted(boolean b) {
                super.setBorderPainted(false);
            }
        };
        button.setOpaque(false);
        button.setForeground(borderColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 139, 34)); // Dark green
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Arial", Font.BOLD, 18)); // Slightly increase font size
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(borderColor);
                button.setFont(new Font("Arial", Font.BOLD, 16)); // Reset font size
            }
        });

        return button;
    }
}