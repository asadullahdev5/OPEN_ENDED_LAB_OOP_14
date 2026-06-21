import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HospitalManagement extends JFrame {

    // Data arrays
    private String[] patientIDs = new String[10];
    private String[] patientNames = new String[10];
    private double[] treatmentCosts = new double[10];
    private int patientCount = 0;

    // Colors
    private final Color BG_DARK      = new Color(13, 27, 42);
    private final Color BG_PANEL     = new Color(27, 38, 59);
    private final Color ACCENT_BLUE  = new Color(0, 180, 216);
    private final Color ACCENT_TEAL  = new Color(72, 202, 228);
    private final Color TEXT_WHITE   = new Color(224, 251, 252);
    private final Color TEXT_MUTED   = new Color(130, 160, 180);
    private final Color PREMIUM_COL  = new Color(255, 200, 60);
    private final Color STANDARD_COL = new Color(100, 210, 130);
    private final Color BASIC_COL    = new Color(130, 160, 210);
    private final Color ERROR_COL    = new Color(255, 80, 80);

    // Input fields
    private JTextField tfID, tfName, tfCost, tfSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblCount, lblRevenue, lblAverage, lblStatus;
    private JTextArea taReport;

    public HospitalManagement() {
        setTitle("Hospital Patient Management System — Iqra University OOP Lab");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 780);
        setLocationRelativeTo(null);
        setBackground(BG_DARK);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        // ── Header ──────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(new EmptyBorder(18, 28, 18, 28));

        JLabel title = new JLabel("🏥  Hospital Patient Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ACCENT_TEAL);

        JLabel sub = new JLabel("OOP Lab Session 14 — Q1  |  Max 10 Patients");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);

        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 2));
        headerText.setOpaque(false);
        headerText.add(title);
        headerText.add(sub);
        header.add(headerText, BorderLayout.WEST);

        lblStatus = new JLabel("Ready");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(ACCENT_BLUE);
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(lblStatus, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ── Center split ─────────────────────────────────────────────
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(370);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);
        split.setBorder(null);
        root.add(split, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        footer.setBackground(BG_PANEL);

        lblCount   = statsLabel("Patients: 0 / 10");
        lblRevenue = statsLabel("Revenue: Rs. 0.00");
        lblAverage = statsLabel("Avg Cost: Rs. 0.00");

        footer.add(lblCount);
        footer.add(sep());
        footer.add(lblRevenue);
        footer.add(sep());
        footer.add(lblAverage);

        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    // ── Left Panel: Input + Search ──────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(14, 14, 14, 7));

        // Input card
        JPanel card = card("Add Patient Record");

        tfID   = styledField("e.g. P-001");
        tfName = styledField("Full name");
        tfCost = styledField("Amount in Rs.");

        card.add(formRow("Patient ID", tfID));
        card.add(Box.createVerticalStrut(8));
        card.add(formRow("Patient Name", tfName));
        card.add(Box.createVerticalStrut(8));
        card.add(formRow("Treatment Cost (Rs.)", tfCost));
        card.add(Box.createVerticalStrut(16));

        JButton btnAdd = accentButton("➕  Add Patient", ACCENT_BLUE);
        btnAdd.addActionListener(e -> addPatient());
        card.add(btnAdd);
        card.add(Box.createVerticalStrut(8));

        JButton btnClear = ghostButton("Clear Fields");
        btnClear.addActionListener(e -> clearFields());
        card.add(btnClear);

        // Search card
        JPanel searchCard = card("Search Patient by Name");
        tfSearch = styledField("Enter patient name…");
        searchCard.add(tfSearch);
        searchCard.add(Box.createVerticalStrut(10));

        JButton btnSearch = accentButton("🔍  Search", new Color(90, 160, 255));
        btnSearch.addActionListener(e -> searchPatient());
        searchCard.add(btnSearch);

        // Actions card
        JPanel actCard = card("Actions");
        JButton btnReport  = accentButton("📋  Generate Report", new Color(60, 180, 120));
        JButton btnClearAll = ghostButton("🗑  Clear All Records");

        btnReport.addActionListener(e -> generateReport());
        btnClearAll.addActionListener(e -> clearAll());

        actCard.add(btnReport);
        actCard.add(Box.createVerticalStrut(8));
        actCard.add(btnClearAll);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        top.add(card);
        top.add(Box.createVerticalStrut(10));
        top.add(searchCard);
        top.add(Box.createVerticalStrut(10));
        top.add(actCard);

        p.add(new JScrollPane(top) {{
            setOpaque(false);
            getViewport().setOpaque(false);
            setBorder(null);
        }}, BorderLayout.CENTER);

        return p;
    }

    // ── Right Panel: Table + Report ─────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(14, 7, 14, 14));

        // Table
        String[] cols = {"ID", "Name", "Cost (Rs.)", "Category"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBackground(BG_PANEL);
        tableScroll.getViewport().setBackground(BG_PANEL);
        tableScroll.setBorder(BorderFactory.createLineBorder(BG_PANEL.brighter(), 1));

        JLabel tableTitle = sectionLabel("Patient Records");
        JPanel tableWrapper = new JPanel(new BorderLayout(0, 6));
        tableWrapper.setOpaque(false);
        tableWrapper.add(tableTitle, BorderLayout.NORTH);
        tableWrapper.add(tableScroll, BorderLayout.CENTER);

        // Report area
        taReport = new JTextArea(10, 30);
        taReport.setEditable(false);
        taReport.setFont(new Font("Consolas", Font.PLAIN, 12));
        taReport.setBackground(BG_PANEL);
        taReport.setForeground(ACCENT_TEAL);
        taReport.setBorder(new EmptyBorder(10, 12, 10, 12));
        taReport.setText("Press 'Generate Report' to view summary…");

        JScrollPane reportScroll = new JScrollPane(taReport);
        reportScroll.setBorder(BorderFactory.createLineBorder(BG_PANEL.brighter(), 1));
        reportScroll.setBackground(BG_PANEL);

        JLabel reportTitle = sectionLabel("Summary Report");
        JPanel reportWrapper = new JPanel(new BorderLayout(0, 6));
        reportWrapper.setOpaque(false);
        reportWrapper.add(reportTitle, BorderLayout.NORTH);
        reportWrapper.add(reportScroll, BorderLayout.CENTER);

        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableWrapper, reportWrapper);
        vSplit.setDividerLocation(370);
        vSplit.setResizeWeight(0.6);
        vSplit.setDividerSize(6);
        vSplit.setBackground(BG_DARK);
        vSplit.setBorder(null);

        p.add(vSplit, BorderLayout.CENTER);
        return p;
    }

    // ── Business Logic ──────────────────────────────────────────────
    private void addPatient() {
        try {
            if (patientCount >= 10) {
                setStatus("❌ Maximum 10 patients reached.", ERROR_COL);
                return;
            }
            String id   = tfID.getText().trim();
            String name = tfName.getText().trim();
            String costStr = tfCost.getText().trim();

            if (id.isEmpty() || name.isEmpty() || costStr.isEmpty()) {
                setStatus("⚠ All fields are required.", ERROR_COL);
                return;
            }

            double cost;
            try {
                cost = Double.parseDouble(costStr);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid numeric input for Treatment Cost.");
            }

            if (cost < 0) {
                throw new IllegalArgumentException("Treatment cost cannot be negative.");
            }

            patientIDs[patientCount]    = id;
            patientNames[patientCount]  = name.toUpperCase(); // String method
            treatmentCosts[patientCount] = cost;
            patientCount++;

            String cat = getCategory(cost);
            tableModel.addRow(new Object[]{id, name.toUpperCase(), String.format("%.2f", cost), cat});

            // Color the category cell
            updateStats();
            clearFields();
            setStatus("✅ Patient added: " + name.toUpperCase() + " (" + cat + ")", ACCENT_BLUE);

        } catch (IllegalArgumentException ex) {
            setStatus("❌ " + ex.getMessage(), ERROR_COL);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPatient() {
        String query = tfSearch.getText().trim().toUpperCase();
        if (query.isEmpty()) {
            setStatus("⚠ Enter a name to search.", ERROR_COL);
            return;
        }
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < patientCount; i++) {
            if (patientNames[i].contains(query)) {
                found = true;
                sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                sb.append("  PATIENT FOUND\n");
                sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                sb.append("  ID       : ").append(patientIDs[i]).append("\n");
                sb.append("  Name     : ").append(patientNames[i]).append("\n");
                sb.append("  Name Len : ").append(patientNames[i].length()).append(" chars\n");
                sb.append("  Cost     : Rs. ").append(String.format("%.2f", treatmentCosts[i])).append("\n");
                sb.append("  Category : ").append(getCategory(treatmentCosts[i])).append("\n");
            }
        }
        if (!found) {
            try {
                throw new RuntimeException("Patient '" + query + "' not found in records.");
            } catch (RuntimeException ex) {
                setStatus("❌ " + ex.getMessage(), ERROR_COL);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        taReport.setText(sb.toString());
        setStatus("🔍 Search result for: " + query, ACCENT_TEAL);
    }

    private void generateReport() {
        if (patientCount == 0) {
            setStatus("⚠ No patient records to report.", ERROR_COL);
            return;
        }
        double total = 0;
        int premium = 0, standard = 0, basic = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════\n");
        sb.append("         HOSPITAL BILLING SUMMARY REPORT      \n");
        sb.append("══════════════════════════════════════════════\n\n");
        sb.append(String.format("  %-6s %-20s %-14s %-10s%n", "ID", "Name", "Cost (Rs.)", "Category"));
        sb.append("  ──────────────────────────────────────────\n");

        for (int i = 0; i < patientCount; i++) {
            total += treatmentCosts[i];
            String cat = getCategory(treatmentCosts[i]);
            if (cat.equals("PREMIUM"))  premium++;
            else if (cat.equals("STANDARD")) standard++;
            else basic++;

            sb.append(String.format("  %-6s %-20s %-14.2f %-10s%n",
                    patientIDs[i], patientNames[i], treatmentCosts[i], cat));
        }

        double avg = total / patientCount;
        sb.append("\n══════════════════════════════════════════════\n");
        sb.append(String.format("  Total Patients   : %d%n", patientCount));
        sb.append(String.format("  Total Revenue    : Rs. %.2f%n", total));
        sb.append(String.format("  Average Cost     : Rs. %.2f%n", avg));
        sb.append("──────────────────────────────────────────────\n");
        sb.append(String.format("  Premium  (>50k)  : %d patient(s)%n", premium));
        sb.append(String.format("  Standard (20-50k): %d patient(s)%n", standard));
        sb.append(String.format("  Basic    (<20k)  : %d patient(s)%n", basic));
        sb.append("══════════════════════════════════════════════\n");

        taReport.setText(sb.toString());
        updateStats();
        setStatus("📋 Report generated for " + patientCount + " patient(s).", ACCENT_TEAL);
    }

    private String getCategory(double cost) {
        if (cost > 50000) return "PREMIUM";
        else if (cost >= 20000) return "STANDARD";
        else return "BASIC";
    }

    private void updateStats() {
        double total = 0;
        for (int i = 0; i < patientCount; i++) total += treatmentCosts[i];
        double avg = patientCount > 0 ? total / patientCount : 0;
        lblCount.setText("Patients: " + patientCount + " / 10");
        lblRevenue.setText("Revenue: Rs. " + String.format("%.2f", total));
        lblAverage.setText("Avg Cost: Rs. " + String.format("%.2f", avg));
    }

    private void clearFields() {
        tfID.setText(""); tfName.setText(""); tfCost.setText("");
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Clear all patient records?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            patientCount = 0;
            tableModel.setRowCount(0);
            taReport.setText("Records cleared.");
            updateStats();
            setStatus("🗑 All records cleared.", TEXT_MUTED);
        }
    }

    // ── UI Helpers ──────────────────────────────────────────────────
    private void styleTable() {
        table.setBackground(BG_PANEL);
        table.setForeground(TEXT_WHITE);
        table.setSelectionBackground(ACCENT_BLUE.darker());
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BG_DARK);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFillsViewportHeight(true);

        JTableHeader th = table.getTableHeader();
        th.setBackground(BG_DARK);
        th.setForeground(ACCENT_TEAL);
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_BLUE));

        // Category cell renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setOpaque(true);
                String v = val != null ? val.toString() : "";
                setBackground(sel ? ACCENT_BLUE.darker() : BG_PANEL);
                switch (v) {
                    case "PREMIUM":  setForeground(PREMIUM_COL);  break;
                    case "STANDARD": setForeground(STANDARD_COL); break;
                    default:         setForeground(BASIC_COL);    break;
                }
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(CENTER);
                return this;
            }
        });

        // Cost right-align
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        right.setBackground(BG_PANEL);
        right.setForeground(TEXT_WHITE);
        table.getColumnModel().getColumn(2).setCellRenderer(right);
    }

    private JPanel card(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE.darker(), 1),
                new EmptyBorder(14, 16, 14, 16)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(ACCENT_TEAL);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        return p;
    }

    private JPanel formRow(String label, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        row.add(lbl, BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    g.setColor(TEXT_MUTED);
                    g.setFont(getFont().deriveFont(Font.ITALIC));
                    g.drawString(placeholder, 8, getHeight() / 2 + 5);
                }
            }
        };
        f.setBackground(BG_DARK);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(ACCENT_BLUE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE.darker(), 1),
                new EmptyBorder(6, 10, 6, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private JButton accentButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(color); }
        });
        return b;
    }

    private JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BG_DARK);
        b.setForeground(TEXT_MUTED);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TEXT_MUTED.darker(), 1),
                new EmptyBorder(7, 14, 7, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(ACCENT_TEAL);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        return l;
    }

    private JLabel statsLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_WHITE);
        return l;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setPreferredSize(new Dimension(1, 16));
        s.setForeground(TEXT_MUTED);
        return s;
    }

    private void setStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(HospitalManagement::new);
    }
}
