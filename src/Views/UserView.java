package Views;

import dao.DataDAO;
import model.Data;

// Standard GUI Imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserView {
    private final String email;
    private JFrame frame;
    private JTable vaultTable;
    private DefaultTableModel tableModel;

    public UserView(String email) {
        this.email = email;
    }

    public void home() {
        // Create Dashboard Window frame
        frame = new JFrame("S.H.U.B.H.A.M. V2.0 - SECURE HUB");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0x1A, 0x1A, 0x1A));

        // Main Base Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(0x1A, 0x1A, 0x1A));

        // --- TOP PANEL: Account Status ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0x22, 0x22, 0x22));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x44, 0x44, 0x44), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel titleLabel = new JLabel("🔒 USER SECURITY DASHBOARD", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel accountLabel = new JLabel("CLEARANCE: " + this.email.toUpperCase(), SwingConstants.RIGHT);
        accountLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        accountLabel.setForeground(new Color(0x00, 0xFF, 0x66));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(accountLabel, BorderLayout.EAST);

        // --- CENTER PANEL: Vault Data Table ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(new Color(0x1A, 0x1A, 0x1A));
        tableContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x33, 0x33, 0x33)),
                " [ SECURE ENCRYPTED ASSETS ] ",
                0, 0,
                new Font("Courier New", Font.BOLD, 12),
                new Color(0x8A, 0x8A, 0x8A)
        ));

        // Set up custom grid table tracking IDs and names
        String[] columns = {"FILE ID", "FILE NAME"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        vaultTable = new JTable(tableModel);
        vaultTable.setFont(new Font("Courier New", Font.PLAIN, 13));
        vaultTable.setRowHeight(24);
        vaultTable.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(vaultTable);
        scrollPane.getViewport().setBackground(new Color(0x22, 0x22, 0x22));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Load initial vault asset listing
        refreshVaultData();

        // --- WEST PANEL: Control Terminal Actions ---
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        actionPanel.setOpaque(false);
        actionPanel.setPreferredSize(new Dimension(220, 0));

        JButton hideBtn = createDashboardButton("🔒 HIDE NEW FILE", new Color(0x33, 0x99, 0xFF));
        JButton restoreBtn = createDashboardButton("🔓 RESTORE ASSET", new Color(0x00, 0xFF, 0x66));
        JButton refreshBtn = createDashboardButton("🔄 REFRESH VAULT", new Color(0xBB, 0xBB, 0xBB));
        JButton logoutBtn = createDashboardButton("🚪 LOGOUT SESSION", new Color(0xFF, 0x33, 0x33));

        actionPanel.add(hideBtn);
        actionPanel.add(restoreBtn);
        actionPanel.add(refreshBtn);
        actionPanel.add(logoutBtn);

        // --- Assemble App Components ---
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.WEST);

        frame.add(mainPanel);

        // --- Functional Action Mappings ---
        hideBtn.addActionListener(e -> hideFileAction());
        restoreBtn.addActionListener(e -> restoreFileAction());
        refreshBtn.addActionListener(e -> refreshVaultData());

        // SAFE CLEAN LOGOUT TRANSITION MATRIX
        logoutBtn.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();

            // Introduces a 150ms structural gap allowing OS rendering pipelines
            // to completely wipe GPU canvas structures down cleanly.
            Timer logoutTimer = new Timer(150, ex -> {
                SwingUtilities.invokeLater(() -> {
                    new Welcome().welcomeScreen();
                });
            });
            logoutTimer.setRepeats(false);
            logoutTimer.start();
        });

        frame.setVisible(true);
    }

    private void refreshVaultData() {
        tableModel.setRowCount(0); // Wipe stale frame data safely
        try {
            List<Data> files = DataDAO.getAllFiles(this.email);
            for (Data file : files) {
                tableModel.addRow(new Object[]{file.getId(), file.getName()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to synch asset tree matrix.", "DB ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hideFileAction() {
        // 1. Swing ka native File Chooser open karo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("SELECT ASSET TO ENCRYPT");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Desktop path auto-open karne ke liye pre-set kar sakte hain
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        // Dialog show karo
        int result = fileChooser.showOpenDialog(frame);

        // Agar user ne 'Cancel' ya window close kar di toh ruk jao
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // 2. Jo file user ne select ki, uska abstract handler uthao
        File f = fileChooser.getSelectedFile();
        String path = f.getAbsolutePath();

        // Safe parsing format conversion (Windows system handle compatibility ke liye)
        path = path.replace("\\", "/");

        // Double check (Aise toh JFileChooser galat path nahi dega, phir bhi safety first)
        if (!f.exists()) {
            JOptionPane.showMessageDialog(frame, "Selected system pointer path invalid.\nPath checked: " + path, "IO ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Purana backend database pipeline logic
        Data file = new Data(0, f.getName(), path, this.email);
        try {
            DataDAO.HideFile(file);
            JOptionPane.showMessageDialog(frame, "Asset fully hidden and locked in pipeline.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            refreshVaultData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database fault during hiding.", "SQL FAULT", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restoreFileAction() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Asset Unique ID To Decrypt:", "RESTORE STORAGE PIPELINE", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            List<Data> files = DataDAO.getAllFiles(this.email);
            boolean isValidID = false;

            for (Data file : files) {
                if (file.getId() == id) {
                    isValidID = true;
                    break;
                }
            }

            if (isValidID) {
                DataDAO.unhide(id);
                JOptionPane.showMessageDialog(frame, "Asset unhidden and redirected back to base route.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                refreshVaultData();
            } else {
                JOptionPane.showMessageDialog(frame, "Target ID input path out of bounding bounds.", "ID ERROR", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "ID format processing mismatch.", "PARSE ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Execution runtime exception on restore.", "EXCEPTION PIPELINE", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createDashboardButton(String text, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Courier New", Font.BOLD, 13));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(new Color(0xBB, 0xBB, 0xBB));
        button.setBackground(new Color(0x2B, 0x2B, 0x2B));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(hoverColor);
                button.setBackground(new Color(0x33, 0x33, 0x33));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(0xBB, 0xBB, 0xBB));
                button.setBackground(new Color(0x2B, 0x2B, 0x2B));
            }
        });
        return button;
    }
}