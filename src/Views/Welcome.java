package Views;

import dao.UserDAO;
import model.user;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;

public class Welcome {

    private JFrame frame;
    // Window dragging track karne ke liye variables
    private int mouseX, mouseY;

    public void welcomeScreen() {
        // 🔥 THE ULTIMATE POPUP LAYER FIX (JDK 26 Compatibility)
        System.setProperty("flatlaf.useWindowDecorations", "false");
        UIManager.put("OptionPane.windowDecorationsByPlatform", Boolean.TRUE);
        JDialog.setDefaultLookAndFeelDecorated(false);
        System.setProperty("flatlaf.menuBarEmbedded", "false");

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());

            // 🌟 CYBERPUNK FOCUS HIGHLIGHTS: Glowing Green focus overrides
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.innerFocusWidth", 1);
            UIManager.put("TextField.focusedBackground", new Color(0x1A, 0x25, 0x1A)); // Subtle Greenish tint
            UIManager.put("Component.focusedBorderColor", new Color(0x00, 0xFF, 0x66)); // Neon Green Glow
            UIManager.put("Button.focusedBorderColor", new Color(0x33, 0x99, 0xFF));   // Glowing Blue Secondary

        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf theme. Falling back to default.");
        }

        frame = new JFrame("S.H.U.B.H.A.M. V2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);
        frame.setLocationRelativeTo(null);

        // 🔥 CRASH FIX: Frame undecorated hai taaki setOpacity() smoothly chal sake
        frame.setUndecorated(true);

        // Anti-Flashing Color Baseline Layer
        frame.getContentPane().setBackground(new Color(0x1A, 0x1A, 0x1A));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0x1A, 0x1A, 0x1A));

        // 🌟 UPGRADE: CYBER GLOW BORDER MATRIX (Multi-Layer Matrix-Green Outline)
        Border neonLine = BorderFactory.createLineBorder(new Color(0x00, 0xFF, 0x66), 1);   // Outer Neon Green Line
        Border darkAccent = BorderFactory.createLineBorder(new Color(0x2A, 0x2A, 0x2A), 2); // Inner Slate/Charcoal Accent
        Border padding = BorderFactory.createEmptyBorder(28, 38, 28, 38);                    // Content Padding

        // Layers ko aapas mein blend karke master border setup kiya
        Border intermediateBorder = BorderFactory.createCompoundBorder(neonLine, darkAccent);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(intermediateBorder, padding));

        // 🌟 WINDOW DRAGGER: Title bar na hone par bhi window smoothly drag ho sakegi
        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                frame.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(0x22, 0x22, 0x22));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x44, 0x44, 0x44), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Text initially blank hai typewriter animation ke liye
        JLabel titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("[Secure Hub for Universal Bit-Hidden Asset Mgmt]", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
        subTitleLabel.setForeground(new Color(0x8A, 0x8A, 0x8A));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(subTitleLabel);

        // --- Button Navigation Panel ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 0, 15));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        // Neon Green hover highlights ke sath animated buttons
        JButton loginBtn = createAnimatedButton("➔  [1] IDENTITY_VERIFICATION  (Login)", new Color(0x2B, 0x2B, 0x2B), new Color(0x00, 0xFF, 0x66));
        JButton signupBtn = createAnimatedButton("➔  [2] NEW_CLEARANCE_ENTRY    (Register)", new Color(0x2B, 0x2B, 0x2B), new Color(0x33, 0x99, 0xFF));
        JButton exitBtn = createAnimatedButton("➔  [0] SYSTEM_TERMINATION     (Shutdown)", new Color(0x2B, 0x2B, 0x2B), new Color(0xFF, 0x33, 0x33));

        menuPanel.add(loginBtn);
        menuPanel.add(signupBtn);
        menuPanel.add(exitBtn);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(menuPanel);

        frame.add(mainPanel);

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> signup());
        exitBtn.addActionListener(e -> System.exit(0));

        // 🌟 ANIMATION 1: Typewriter Effect Trigger
        startTypewriterAnimation(titleLabel, "S . H . U . B . h . a . m .  (V 2.0)");

        // 🌟 ANIMATION 2: Window Smooth Fade-In on Launch
        frame.setOpacity(0.0f);
        frame.setVisible(true);

        Timer fadeInTimer = new Timer(15, new java.awt.event.ActionListener() {
            private float opacity = 0.0f;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    frame.setOpacity(1.0f);
                    ((Timer) e.getSource()).stop();
                } else {
                    frame.setOpacity(opacity);
                }
            }
        });
        fadeInTimer.start();
    }

    // 🌟 ANIMATION SYSTEM: Smooth Color Fade Hover Effect
    private JButton createAnimatedButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Courier New", Font.BOLD, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(new Color(0xBB, 0xBB, 0xBB));
        button.setBackground(baseColor);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer timer;
            private float alpha = 0.0f; // 0 = Base Color, 1 = Hover Color

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (timer != null && timer.isRunning()) timer.stop();
                timer = new Timer(15, e -> {
                    alpha += 0.1f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        timer.stop();
                    }
                    button.setBackground(interpolateColor(baseColor, hoverColor, alpha));
                    button.setForeground(Color.WHITE);
                });
                timer.start();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (timer != null && timer.isRunning()) timer.stop();
                timer = new Timer(15, e -> {
                    alpha -= 0.1f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        timer.stop();
                    }
                    button.setBackground(interpolateColor(baseColor, hoverColor, alpha));
                    button.setForeground(new Color(0xBB, 0xBB, 0xBB));
                });
                timer.start();
            }
        });
        return button;
    }

    private Color interpolateColor(Color color1, Color color2, float fraction) {
        int r = (int) (color1.getRed() + fraction * (color2.getRed() - color1.getRed()));
        int g = (int) (color1.getGreen() + fraction * (color2.getGreen() - color1.getGreen()));
        int b = (int) (color1.getBlue() + fraction * (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b);
    }

    private void startTypewriterAnimation(JLabel label, String fullText) {
        Timer typewriterTimer = new Timer(60, new java.awt.event.ActionListener() {
            private int charIndex = 0;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (charIndex <= fullText.length()) {
                    label.setText(fullText.substring(0, charIndex));
                    charIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        typewriterTimer.start();
    }

    private void login() {
        String email = JOptionPane.showInputDialog(frame, "Enter Email:", "IDENTITY VERIFICATION", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;

        try {
            if (UserDAO.isExists(email)) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);

                String otp = JOptionPane.showInputDialog(frame, "Enter the OTP sent to your email:", "OTP VERIFICATION", JOptionPane.WARNING_MESSAGE);

                if (otp != null && otp.equals(genOTP)) {
                    frame.setVisible(false);
                    frame.dispose();

                    Timer transitionTimer = new Timer(150, e -> {
                        SwingUtilities.invokeLater(() -> {
                            UserView dashboard = new UserView(email);
                            dashboard.home();
                        });
                    });
                    transitionTimer.setRepeats(false);
                    transitionTimer.start();

                } else if (otp != null) {
                    JOptionPane.showMessageDialog(frame, "Access Denied: Invalid OTP", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "User Not Found In Database", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database connectivity error.", "EXCEPTION THROWN", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signup() {
        String name = JOptionPane.showInputDialog(frame, "Enter Name:", "NEW CLEARANCE REGISTRATION", JOptionPane.QUESTION_MESSAGE);
        if (name == null || name.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog(frame, "Enter Email:", "NEW CLEARANCE REGISTRATION", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;

        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);

        String otp = JOptionPane.showInputDialog(frame, "Enter the OTP sent to your email:", "OTP VERIFICATION", JOptionPane.WARNING_MESSAGE);

        if (otp != null && otp.equals(genOTP)) {
            user user = new user(name, email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 0 -> JOptionPane.showMessageDialog(frame, "User Already Existed", "REGISTRATION INFO", JOptionPane.INFORMATION_MESSAGE);
                case 1 -> JOptionPane.showMessageDialog(frame, "User registered successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (otp != null) {
            JOptionPane.showMessageDialog(frame, "Registration Denied: Invalid OTP", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}