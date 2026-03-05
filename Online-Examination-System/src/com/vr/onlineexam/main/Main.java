// Main.java
//
// Launcher that integrates the persistent UserRegistry (users.db) with the Login / Registration / Profile dialogs.
// - Uses UserRegistry (file-backed) for register/login/profile changes.
// - Uses ThemeManager for theme initialization.
// - Launches ModernExamUI after successful login or guest continue.
// Save as UTF-8 (no BOM) and place into src/Main.java (overwrite existing Main.java).
package com.vr.onlineexam.main;
import javax.swing.*;
import java.awt.*;

/**
 * Main launcher that shows a Login dialog backed by UserRegistry (users.db).
 * After successful login (or guest), it launches ModernExamUI.
 *
 * Note: UserRegistry.java must already be present in src/ and compiled.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // system look and feel baseline
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            // theme defaults (safe)
            try { ThemeManager.init(); } catch (Throwable ignored) {}

            // persistent registry (users.db)
            UserRegistry registry = new UserRegistry();

            // show login dialog
            LoginDialog login = new LoginDialog(null, registry);
            login.setVisible(true);

            if (!login.isAuthenticated()) {
                int r = JOptionPane.showConfirmDialog(null, "Continue as guest?", "Continue?", JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            // get authenticated user (may be null for guest)
            UserRegistry.User user = login.getAuthenticatedUser();

            try {
                ModernExamUI ui = new ModernExamUI();
                ui.showUI();
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(null, "Startup failed: " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

/** Login dialog that uses UserRegistry for validation and registration. */
class LoginDialog extends JDialog {
    private final JTextField tfUser = new JTextField(18);
    private final JPasswordField pf = new JPasswordField(18);
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnGuest = new JButton("Continue as Guest");
    private final JButton btnProfile = new JButton("Update Profile");
    private final JButton btnRegister = new JButton("Register");
    private final JLabel status = new JLabel("Tip: register or use a created account.");
    private final UserRegistry registry;

    private boolean authenticated = false;
    private UserRegistry.User authenticatedUser = null;

    LoginDialog(Frame owner, UserRegistry registry) {
        super(owner, "Sign in — OASIS Internship Exam", true);
        this.registry = registry;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10,10));
        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.gridx = 0; g.gridy = 0; g.anchor = GridBagConstraints.EAST;
        center.add(new JLabel("Username:"), g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        tfUser.setFont(ThemeManager.uiFont(13f, Font.PLAIN));
        center.add(tfUser, g);

        g.gridx = 0; g.gridy = 1; g.anchor = GridBagConstraints.EAST;
        center.add(new JLabel("Password:"), g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        pf.setFont(ThemeManager.uiFont(13f, Font.PLAIN));
        center.add(pf, g);

        // action row: Profile | Register | Guest | Login
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        btnProfile.setFont(ThemeManager.uiFont(12f, Font.PLAIN));
        btnRegister.setFont(ThemeManager.uiFont(12f, Font.PLAIN));
        btnGuest.setFont(ThemeManager.uiFont(12f, Font.PLAIN));
        btnLogin.setFont(ThemeManager.uiFont(13f, Font.BOLD));

        actions.add(btnProfile);
        actions.add(btnRegister);
        actions.add(btnGuest);
        actions.add(btnLogin);

        status.setFont(ThemeManager.uiFont(12f, Font.PLAIN));
        status.setForeground(Color.DARK_GRAY);

        add(center, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        add(status, BorderLayout.NORTH);

        // handlers
        btnLogin.addActionListener(e -> attemptLogin());
        btnGuest.addActionListener(e -> continueAsGuest());
        btnProfile.addActionListener(e -> openProfileDialog());
        btnRegister.addActionListener(e -> openRegistrationDialog());

        getRootPane().setDefaultButton(btnLogin);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    private void attemptLogin() {
        String u = tfUser.getText().trim();
        String p = new String(pf.getPassword());
        if (u.isEmpty()) {
            status.setText("Enter username.");
            return;
        }
        if (registry.validate(u, p)) {
            authenticated = true;
            authenticatedUser = registry.getUser(u);
            dispose();
            return;
        }
        // fallback: accept "intern"/"intern123" for convenience (no registry)
        if ("intern".equals(u) && "intern123".equals(p)) {
            authenticated = true;
            authenticatedUser = new UserRegistry.User("intern", "Intern (Guest)", "intern123");
            dispose();
            return;
        }
        status.setText("Invalid credentials. Try registering or use intern/intern123.");
    }

    private void continueAsGuest() {
        authenticated = true;
        authenticatedUser = new UserRegistry.User("guest", "Guest User", "");
        dispose();
    }

    private void openProfileDialog() {
        String u = tfUser.getText().trim();
        UserRegistry.User user = (u.isEmpty() ? null : registry.getUser(u));
        ProfileDialog pd = new ProfileDialog((Frame)getOwner(), registry, user);
        pd.setVisible(true);
    }

    private void openRegistrationDialog() {
        RegistrationDialog rd = new RegistrationDialog((Frame)getOwner(), registry);
        rd.setVisible(true);
    }

    boolean isAuthenticated() { return authenticated; }
    UserRegistry.User getAuthenticatedUser() { return authenticatedUser; }
}

/** Registration dialog that persists new users to UserRegistry. */
class RegistrationDialog extends JDialog {
    RegistrationDialog(Frame owner, UserRegistry registry) {
        super(owner, "Register — OASIS Exam", true);
        initUI(registry);
    }

    private void initUI(UserRegistry registry) {
        setLayout(new BorderLayout(8,8));
        JPanel grid = new JPanel(new GridLayout(0,2,8,8));
        grid.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JTextField tfUser = new JTextField();
        JTextField tfName = new JTextField();
        JPasswordField pfPass = new JPasswordField();
        JPasswordField pfConfirm = new JPasswordField();

        grid.add(new JLabel("Username:")); grid.add(tfUser);
        grid.add(new JLabel("Display name:")); grid.add(tfName);
        grid.add(new JLabel("Password:")); grid.add(pfPass);
        grid.add(new JLabel("Confirm password:")); grid.add(pfConfirm);

        JLabel status = new JLabel(" ");
        status.setFont(ThemeManager.uiFont(12f, Font.PLAIN));

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> {
            String u = tfUser.getText().trim();
            String n = tfName.getText().trim();
            String p = new String(pfPass.getPassword());
            String c = new String(pfConfirm.getPassword());
            if (u.isEmpty() || p.isEmpty() || n.isEmpty()) {
                status.setText("All fields are required.");
                return;
            }
            if (!p.equals(c)) {
                status.setText("Passwords do not match.");
                return;
            }
            boolean ok = registry.register(u, n, p);
            if (!ok) {
                status.setText("Username already exists or invalid.");
                return;
            }
            registry.save();
            JOptionPane.showMessageDialog(this, "Registered successfully. You can now login.", "Registered", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        foot.add(btnCancel);
        foot.add(btnRegister);

        add(grid, BorderLayout.CENTER);
        add(status, BorderLayout.NORTH);
        add(foot, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
    }
}

/** Profile dialog allowing display name change and password update for a loaded user. */
class ProfileDialog extends JDialog {
    ProfileDialog(Frame owner, UserRegistry registry, UserRegistry.User user) {
        super(owner, "Update Profile", true);
        initUI(registry, user);
    }

    private void initUI(UserRegistry registry, UserRegistry.User user) {
        setLayout(new BorderLayout(8,8));
        JPanel p = new JPanel(new GridLayout(0,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextField tfName = new JTextField(user != null ? user.displayName : "");
        JPasswordField pfOld = new JPasswordField();
        JPasswordField pfNew = new JPasswordField();

        p.add(new JLabel("Display name:"));
        p.add(tfName);
        p.add(new JLabel("Old password (to change):"));
        p.add(pfOld);
        p.add(new JLabel("New password:"));
        p.add(pfNew);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            String newName = tfName.getText().trim();
            if (!newName.isEmpty() && user != null) user.displayName = newName;
            String oldp = new String(pfOld.getPassword());
            String newp = new String(pfNew.getPassword());
            if (!oldp.isEmpty() || !newp.isEmpty()) {
                if (user == null || !user.password.equals(oldp)) {
                    JOptionPane.showMessageDialog(this, "Old password incorrect or no user loaded.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                user.password = newp;
            }
            // persist change
            registry.save();
            JOptionPane.showMessageDialog(this, "Profile updated (saved).", "Saved", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        foot.add(cancel);
        foot.add(save);

        add(p, BorderLayout.CENTER);
        add(foot, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
    }
}
