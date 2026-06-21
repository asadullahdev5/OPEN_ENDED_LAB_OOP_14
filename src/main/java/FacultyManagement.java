import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// ── Package-level interfaces & abstract classes ─────────────────────────────

interface BonusPolicy {
    double calculateBonus();
}

abstract class Employee implements BonusPolicy {
    private   int    employeeId;
    protected String employeeName;
    protected double basicSalary;
    private   String employeeType;

    // Default constructor
    public Employee() {
        this.employeeId   = 0;
        this.employeeName = "Unknown";
        this.basicSalary  = 0.0;
        this.employeeType = "Employee";
    }

    // Parameterized constructor
    public Employee(int id, String name, double basicSalary, String type) {
        this.employeeId   = id;
        this.employeeName = name;
        this.basicSalary  = basicSalary;
        this.employeeType = type;
    }

    public abstract double calculateSalary();

    public int    getEmployeeId()   { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public double getBasicSalary()  { return basicSalary; }
    public String getEmployeeType() { return employeeType; }
}

class PermanentFaculty extends Employee {
    private double housingAllowance = 0.30;
    private double medicalAllowance = 0.15;
    private double bonusRate        = 0.20;

    public PermanentFaculty() { super(); }

    public PermanentFaculty(int id, String name, double basicSalary) {
        super(id, name, basicSalary, "Permanent Faculty");
    }

    @Override
    public double calculateSalary() {
        return basicSalary + (basicSalary * housingAllowance) + (basicSalary * medicalAllowance);
    }

    @Override
    public double calculateBonus() {
        return basicSalary * bonusRate;
    }
}

class VisitingFaculty extends Employee {
    private int    hoursPerWeek;
    private double hourlyRate;
    private double bonusRate = 0.05;

    public VisitingFaculty() { super(); hoursPerWeek = 0; hourlyRate = 0; }

    public VisitingFaculty(int id, String name, double basicSalary, int hoursPerWeek, double hourlyRate) {
        super(id, name, basicSalary, "Visiting Faculty");
        this.hoursPerWeek = hoursPerWeek;
        this.hourlyRate   = hourlyRate;
    }

    @Override
    public double calculateSalary() {
        return hoursPerWeek * hourlyRate * 4; // monthly (4 weeks)
    }

    @Override
    public double calculateBonus() {
        return calculateSalary() * bonusRate;
    }
}

class LabInstructor extends Employee {
    private double labAllowance = 0.10;
    private double bonusRate    = 0.10;

    public LabInstructor() { super(); }

    public LabInstructor(int id, String name, double basicSalary) {
        super(id, name, basicSalary, "Lab Instructor");
    }

    @Override
    public double calculateSalary() {
        return basicSalary + (basicSalary * labAllowance);
    }

    @Override
    public double calculateBonus() {
        return basicSalary * bonusRate;
    }
}

// ── Main GUI Application ────────────────────────────────────────────────────

public class FacultyManagement extends JFrame {

    private Employee[] employees = new Employee[20];
    private int        empCount  = 0;

    // Design tokens
    private final Color BG_DARK   = new Color(15, 20, 35);
    private final Color BG_PANEL  = new Color(25, 32, 52);
    private final Color BG_CARD   = new Color(30, 40, 65);
    private final Color GOLD      = new Color(255, 190, 50);
    private final Color PURPLE    = new Color(160, 100, 255);
    private final Color CYAN      = new Color(60, 210, 200);
    private final Color GREEN     = new Color(60, 200, 120);
    private final Color TEXT      = new Color(220, 228, 248);
    private final Color MUTED     = new Color(100, 120, 160);
    private final Color ERROR_COL = new Color(255, 80, 80);

    // Permanent colors per type
    private final Color C_PERM  = new Color(80, 160, 255);
    private final Color C_VISIT = new Color(255, 140, 80);
    private final Color C_LAB   = new Color(120, 220, 100);

    // Form fields
    private JTextField tfId, tfName, tfBasic;
    private JComboBox<String> cbType;
    private JTextField tfHours, tfHourly;
    private JLabel lblHours, lblHourly;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea taReport;
    private JLabel lblStatus, lblTotal, lblCount;

    public FacultyManagement() {
        setTitle("Faculty Management System — Iqra University OOP Lab");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 820);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        root.add(buildHeader(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(400);
        split.setDividerSize(5);
        split.setBorder(null);
        split.setBackground(BG_DARK);
        root.add(split, BorderLayout.CENTER);

        root.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_PANEL);
        h.setBorder(new EmptyBorder(16, 28, 16, 28));

        JLabel title = new JLabel("🎓  Faculty Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(GOLD);

        JLabel sub = new JLabel("OOP Lab Session 14 — Q2  |  Abstract Classes · Interfaces · Polymorphism");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(MUTED);

        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 3));
        txt.setOpaque(false);
        txt.add(title); txt.add(sub);
        h.add(txt, BorderLayout.WEST);

        lblStatus = new JLabel("Ready");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(CYAN);
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        h.add(lblStatus, BorderLayout.EAST);
        return h;
    }

    private JPanel buildLeftPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(14, 14, 14, 7));

        // ── Add Employee Card ──
        JPanel card = card("Add Employee");

        tfId    = field("e.g. 101");
        tfName  = field("Full name");
        tfBasic = field("Monthly basic salary (Rs.)");
        cbType  = new JComboBox<>(new String[]{"Permanent Faculty", "Visiting Faculty", "Lab Instructor"});
        styleCombo(cbType);

        // Visiting-only fields
        tfHours  = field("Hours per week");
        tfHourly = field("Hourly rate (Rs.)");
        lblHours  = fieldLabel("Hours/Week");
        lblHourly = fieldLabel("Hourly Rate (Rs.)");

        card.add(formRow("Employee ID",     tfId));
        card.add(strut(8));
        card.add(formRow("Employee Name",   tfName));
        card.add(strut(8));
        card.add(formRow("Type",            cbType));
        card.add(strut(8));
        card.add(formRow("Basic Salary (Rs.)", tfBasic));
        card.add(strut(8));

        JPanel visitExtra = new JPanel();
        visitExtra.setLayout(new BoxLayout(visitExtra, BoxLayout.Y_AXIS));
        visitExtra.setOpaque(false);
        visitExtra.add(formRow2(lblHours, tfHours));
        visitExtra.add(strut(8));
        visitExtra.add(formRow2(lblHourly, tfHourly));
        visitExtra.setVisible(false);
        card.add(visitExtra);
        card.add(strut(4));

        cbType.addActionListener(e -> {
            boolean visiting = cbType.getSelectedIndex() == 1;
            visitExtra.setVisible(visiting);
            tfBasic.setEnabled(!visiting);
            if (visiting) tfBasic.setText("");
            revalidate();
        });

        card.add(strut(10));
        JButton btnAdd = btn("➕  Add Employee", PURPLE);
        btnAdd.addActionListener(e -> addEmployee());
        card.add(btnAdd);
        card.add(strut(8));
        JButton btnClear = ghostBtn("Clear Fields");
        btnClear.addActionListener(e -> clearFields());
        card.add(btnClear);

        // ── Actions Card ──
        JPanel acts = card("Actions");
        JButton btnReport   = btn("📋  Generate Payroll Report", GREEN);
        JButton btnClearAll = ghostBtn("🗑  Clear All Records");
        btnReport.addActionListener(e -> generateReport());
        btnClearAll.addActionListener(e -> clearAll());
        acts.add(btnReport);
        acts.add(strut(8));
        acts.add(btnClearAll);

        // ── OOP Concepts Card ──
        JPanel oop = card("OOP Concepts Demonstrated");
        String[] concepts = {
            "✔ Abstract class: Employee",
            "✔ Concrete subclasses: 3 types",
            "✔ Interface: BonusPolicy",
            "✔ Method overriding: calculateSalary()",
            "✔ Constructor overloading",
            "✔ Runtime polymorphism",
            "✔ Access modifiers: private / protected / public"
        };
        for (String c : concepts) {
            JLabel lbl = new JLabel(c);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(CYAN);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            oop.add(lbl);
            oop.add(strut(3));
        }

        p.add(card);
        p.add(strut(10));
        p.add(acts);
        p.add(strut(10));
        p.add(oop);

        JScrollPane sp = new JScrollPane(p);
        sp.setBorder(null);
        sp.setBackground(BG_DARK);
        sp.getViewport().setBackground(BG_DARK);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG_DARK);
        wrap.add(sp);
        return wrap;
    }

    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(14, 7, 14, 14));

        // Table
        String[] cols = {"ID", "Name", "Type", "Basic (Rs.)", "Net Salary (Rs.)", "Bonus (Rs.)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane tsp = new JScrollPane(table);
        tsp.setBackground(BG_PANEL);
        tsp.getViewport().setBackground(BG_PANEL);
        tsp.setBorder(BorderFactory.createLineBorder(PURPLE.darker(), 1));

        JPanel tw = new JPanel(new BorderLayout(0, 6));
        tw.setOpaque(false);
        tw.add(sectionLabel("Employee Records"), BorderLayout.NORTH);
        tw.add(tsp, BorderLayout.CENTER);

        // Report
        taReport = new JTextArea(12, 40);
        taReport.setEditable(false);
        taReport.setFont(new Font("Consolas", Font.PLAIN, 12));
        taReport.setBackground(BG_PANEL);
        taReport.setForeground(GOLD);
        taReport.setBorder(new EmptyBorder(10, 14, 10, 14));
        taReport.setText("Press 'Generate Payroll Report' to view full summary…");

        JScrollPane rsp = new JScrollPane(taReport);
        rsp.setBorder(BorderFactory.createLineBorder(GOLD.darker(), 1));

        JPanel rw = new JPanel(new BorderLayout(0, 6));
        rw.setOpaque(false);
        rw.add(sectionLabel("Payroll Report"), BorderLayout.NORTH);
        rw.add(rsp, BorderLayout.CENTER);

        JSplitPane vs = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tw, rw);
        vs.setDividerLocation(390);
        vs.setResizeWeight(0.55);
        vs.setDividerSize(5);
        vs.setBorder(null);
        vs.setBackground(BG_DARK);

        p.add(vs, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        f.setBackground(BG_PANEL);
        lblCount = statsLbl("Employees: 0");
        lblTotal  = statsLbl("Total Payroll: Rs. 0.00");
        f.add(lblCount);
        f.add(vsep());
        f.add(lblTotal);
        return f;
    }

    // ── Business Logic ─────────────────────────────────────────────

    private void addEmployee() {
        try {
            if (empCount >= 20) {
                status("❌ Maximum 20 employees reached.", ERROR_COL); return;
            }
            String idStr = tfId.getText().trim();
            String name  = tfName.getText().trim();
            String typeStr = (String) cbType.getSelectedItem();

            if (idStr.isEmpty() || name.isEmpty()) {
                status("⚠ ID and Name are required.", ERROR_COL); return;
            }

            int id;
            try { id = Integer.parseInt(idStr); }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Employee ID must be a valid integer.");
            }

            Employee emp;
            double salary, bonus;

            if (typeStr.equals("Visiting Faculty")) {
                String hStr = tfHours.getText().trim(), rStr = tfHourly.getText().trim();
                if (hStr.isEmpty() || rStr.isEmpty()) throw new IllegalArgumentException("Hours/Week and Hourly Rate required for Visiting Faculty.");
                int hrs; double rate;
                try { hrs = Integer.parseInt(hStr); rate = Double.parseDouble(rStr); }
                catch (NumberFormatException ex) { throw new IllegalArgumentException("Hours and Rate must be numeric."); }
                if (hrs < 0 || rate < 0) throw new IllegalArgumentException("Hours and Rate cannot be negative.");
                emp = new VisitingFaculty(id, name, 0, hrs, rate);
            } else {
                String bStr = tfBasic.getText().trim();
                if (bStr.isEmpty()) throw new IllegalArgumentException("Basic Salary is required.");
                double basic;
                try { basic = Double.parseDouble(bStr); }
                catch (NumberFormatException ex) { throw new IllegalArgumentException("Basic Salary must be numeric."); }
                if (basic < 0) throw new IllegalArgumentException("Salary cannot be negative.");
                if (typeStr.equals("Permanent Faculty")) emp = new PermanentFaculty(id, name, basic);
                else emp = new LabInstructor(id, name, basic);
            }

            salary = emp.calculateSalary();
            bonus  = emp.calculateBonus();
            employees[empCount++] = emp;

            tableModel.addRow(new Object[]{
                emp.getEmployeeId(),
                emp.getEmployeeName(),
                emp.getEmployeeType(),
                String.format("%.2f", emp.getBasicSalary()),
                String.format("%.2f", salary),
                String.format("%.2f", bonus)
            });

            updateFooter();
            clearFields();
            status("✅ Added: " + emp.getEmployeeName() + " — " + emp.getEmployeeType(), CYAN);

        } catch (IllegalArgumentException ex) {
            status("❌ " + ex.getMessage(), ERROR_COL);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReport() {
        if (empCount == 0) { status("⚠ No employees to report.", ERROR_COL); return; }

        double totalPayroll = 0, totalBonus = 0;
        int perm = 0, visit = 0, lab = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════════════════════════\n");
        sb.append("               UNIVERSITY FACULTY PAYROLL REPORT                 \n");
        sb.append("                     Iqra University — OOP Lab                   \n");
        sb.append("══════════════════════════════════════════════════════════════════\n\n");
        sb.append(String.format("  %-4s %-20s %-20s %-12s %-14s %-12s%n",
                "ID","Name","Type","Basic (Rs.)","Salary (Rs.)","Bonus (Rs.)"));
        sb.append("  ──────────────────────────────────────────────────────────────\n");

        // Runtime polymorphism: Employee references
        for (int i = 0; i < empCount; i++) {
            Employee e = employees[i]; // Employee reference → overridden method called
            double sal = e.calculateSalary();
            double bon = e.calculateBonus();
            totalPayroll += sal;
            totalBonus   += bon;
            String t = e.getEmployeeType();
            if (t.contains("Permanent")) perm++;
            else if (t.contains("Visiting")) visit++;
            else lab++;

            sb.append(String.format("  %-4d %-20s %-20s %-12.2f %-14.2f %-12.2f%n",
                    e.getEmployeeId(), e.getEmployeeName(), t,
                    e.getBasicSalary(), sal, bon));
        }

        sb.append("\n══════════════════════════════════════════════════════════════════\n");
        sb.append(String.format("  Total Employees     : %d%n", empCount));
        sb.append(String.format("  Total Payroll       : Rs. %.2f%n", totalPayroll));
        sb.append(String.format("  Total Bonus Payout  : Rs. %.2f%n", totalBonus));
        sb.append(String.format("  Grand Total         : Rs. %.2f%n", totalPayroll + totalBonus));
        sb.append("──────────────────────────────────────────────────────────────────\n");
        sb.append(String.format("  Permanent Faculty   : %d%n", perm));
        sb.append(String.format("  Visiting Faculty    : %d%n", visit));
        sb.append(String.format("  Lab Instructors     : %d%n", lab));
        sb.append("══════════════════════════════════════════════════════════════════\n");
        sb.append("\n  [Runtime Polymorphism]: calculateSalary() & calculateBonus()\n");
        sb.append("  called through Employee[] array references at runtime.\n");

        taReport.setText(sb.toString());
        updateFooter();
        status("📋 Payroll report generated for " + empCount + " employee(s).", GOLD);
    }

    private void updateFooter() {
        double total = 0;
        for (int i = 0; i < empCount; i++) total += employees[i].calculateSalary();
        lblCount.setText("Employees: " + empCount);
        lblTotal.setText("Total Payroll: Rs. " + String.format("%.2f", total));
    }

    private void clearFields() {
        tfId.setText(""); tfName.setText(""); tfBasic.setText("");
        tfHours.setText(""); tfHourly.setText("");
        cbType.setSelectedIndex(0);
        tfBasic.setEnabled(true);
    }

    private void clearAll() {
        int c = JOptionPane.showConfirmDialog(this, "Clear all employee records?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            empCount = 0;
            tableModel.setRowCount(0);
            taReport.setText("Records cleared.");
            updateFooter();
            status("🗑 All records cleared.", MUTED);
        }
    }

    // ── UI helpers ─────────────────────────────────────────────────

    private void styleTable() {
        table.setBackground(BG_PANEL);
        table.setForeground(TEXT);
        table.setSelectionBackground(PURPLE.darker());
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BG_DARK);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setFillsViewportHeight(true);

        JTableHeader th = table.getTableHeader();
        th.setBackground(BG_DARK);
        th.setForeground(GOLD);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GOLD));

        // Type column renderer
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setOpaque(true);
                setBackground(sel ? PURPLE.darker() : BG_PANEL);
                String v = val != null ? val.toString() : "";
                if (v.contains("Permanent"))  setForeground(C_PERM);
                else if (v.contains("Visiting")) setForeground(C_VISIT);
                else setForeground(C_LAB);
                setFont(new Font("Segoe UI", Font.BOLD, 11));
                return this;
            }
        });

        // Right-align numeric columns
        DefaultTableCellRenderer right = new DefaultTableCellRenderer() {
            { setBackground(BG_PANEL); setForeground(TEXT); setHorizontalAlignment(RIGHT); }
        };
        for (int c : new int[]{3, 4, 5}) table.getColumnModel().getColumn(c).setCellRenderer(right);
    }

    private JPanel card(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PURPLE.darker(), 1),
                new EmptyBorder(14, 16, 14, 16)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(GOLD);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        return p;
    }

    private JTextField field(String ph) {
        JTextField f = new JTextField() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    g.setColor(MUTED); g.setFont(getFont().deriveFont(Font.ITALIC));
                    g.drawString(ph, 8, getHeight()/2+5);
                }
            }
        };
        f.setBackground(BG_DARK); f.setForeground(TEXT);
        f.setCaretColor(GOLD);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PURPLE.darker(), 1),
                new EmptyBorder(5, 8, 5, 8)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        f.setAlignmentX(LEFT_ALIGNMENT);
        return f;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setBackground(BG_DARK); cb.setForeground(TEXT);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cb.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JPanel formRow(String label, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(0, 3));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = fieldLabel(label);
        row.add(lbl, BorderLayout.NORTH);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }

    private JPanel formRow2(JLabel lbl, JTextField comp) {
        JPanel row = new JPanel(new BorderLayout(0, 3));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.add(lbl, BorderLayout.NORTH);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(MUTED);
        return l;
    }

    private JButton btn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(color); }
        });
        return b;
    }

    private JButton ghostBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(BG_DARK); b.setForeground(MUTED);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MUTED.darker(), 1), new EmptyBorder(6, 14, 6, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        b.setAlignmentX(LEFT_ALIGNMENT);
        return b;
    }

    private JLabel sectionLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(GOLD);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        return l;
    }

    private JLabel statsLbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT);
        return l;
    }

    private Component strut(int h) { return Box.createVerticalStrut(h); }

    private JSeparator vsep() {
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setPreferredSize(new Dimension(1, 16));
        s.setForeground(MUTED);
        return s;
    }

    private void status(String msg, Color color) {
        lblStatus.setText(msg); lblStatus.setForeground(color);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(FacultyManagement::new);
    }
}
