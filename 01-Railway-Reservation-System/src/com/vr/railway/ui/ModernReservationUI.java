package com.venkatram.oibsip.task1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * ModernReservationUI - polished FAANG-style Swing UI for reservation system
 * Author: Venkatram
 */
public class ModernReservationUI {

    private final JFrame frame;
    private final ReservationService service = new ReservationService();
    private final ReservationRepository repository = new ReservationRepository();

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> reservationList = new JList<>(listModel);

    public ModernReservationUI() {
        frame = new JFrame("OIBSIP — Railway Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 680);
        frame.setMinimumSize(new Dimension(920, 560));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(0, 0));

        ThemeManager.apply(); // apply current theme values
        frame.add(buildHeader(), BorderLayout.NORTH);
        frame.add(buildBody(), BorderLayout.CENTER);

        refreshList(); // populate list at start
    }

    public void showUI() {
        SwingUtilities.invokeLater(() -> {
            ThemeManager.applyToComponentTree(frame);
            frame.setVisible(true);
        });
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(12, 18, 12, 18));
        header.setOpaque(true);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        left.setOpaque(false);
        JLabel brand = new JLabel("OIBSIP");
        brand.setFont(ThemeManager.uiFont(20f, Font.BOLD));
        brand.setForeground(new Color(26, 115, 232));
        JLabel title = new JLabel("RAILWAY RESERVATION SYSTEM");
        title.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        left.add(brand);
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        right.setOpaque(false);

        JToggleButton themeToggle = new JToggleButton(ThemeManager.isDark() ? "Dark" : "Light");
        themeToggle.setSelected(ThemeManager.isDark());
        themeToggle.addActionListener(e -> {
            ThemeManager.toggle();
            ThemeManager.apply();
            ThemeManager.applyToComponentTree(frame);
            themeToggle.setText(ThemeManager.isDark() ? "Dark" : "Light");
        });

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshList());

        right.add(refreshBtn);
        right.add(themeToggle);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, ThemeManager.isDark() ? new Color(60,60,60) : new Color(220,220,220)));
        return header;
    }

    private JComponent buildBody() {
        JPanel container = new JPanel(new BorderLayout(12,12));
        container.setBorder(new EmptyBorder(14,14,14,14));
        container.setOpaque(false);

        container.add(buildLeftPanel(), BorderLayout.WEST);
        container.add(buildCenterPanel(), BorderLayout.CENTER);
        container.add(buildRightPanel(), BorderLayout.EAST);

        return container;
    }

    private JComponent buildLeftPanel() {
        RoundedPanel leftCol = new RoundedPanel(14);
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setBorder(new EmptyBorder(16,16,16,16));
        leftCol.setPreferredSize(new Dimension(260, 0));

        JLabel heading = new JLabel("Quick Actions");
        heading.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        leftCol.add(heading);
        leftCol.add(Box.createVerticalStrut(12));

        JButton book = new JButton("Book Ticket");
        stylePrimary(book);
        book.setAlignmentX(Component.LEFT_ALIGNMENT);
        book.addActionListener(e -> openBookDialog());

        JButton view = new JButton("View Ticket");
        view.setAlignmentX(Component.LEFT_ALIGNMENT);
        view.addActionListener(e -> openViewDialog());

        JButton cancel = new JButton("Cancel Ticket");
        cancel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancel.addActionListener(e -> openCancelDialog());

        leftCol.add(book);
        leftCol.add(Box.createVerticalStrut(8));
        leftCol.add(view);
        leftCol.add(Box.createVerticalStrut(8));
        leftCol.add(cancel);
        leftCol.add(Box.createVerticalStrut(16));

        JButton export = new JButton("Export CSV");
        export.setAlignmentX(Component.LEFT_ALIGNMENT);
        export.addActionListener(e -> exportCsv());
        leftCol.add(export);

        leftCol.add(Box.createVerticalGlue());
        return leftCol;
    }

    private JComponent buildCenterPanel() {
        RoundedPanel center = new RoundedPanel(14);
        center.setLayout(new BorderLayout(10,10));
        center.setBorder(new EmptyBorder(12,12,12,12));

        JLabel h = new JLabel("Reservations");
        h.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        center.add(h, BorderLayout.NORTH);

        reservationList.setModel(listModel);
        reservationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationList.setFont(ThemeManager.uiFont(13f, Font.PLAIN));
        reservationList.setFixedCellHeight(36);
        JScrollPane sc = new JScrollPane(reservationList);
        center.add(sc, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        JButton details = new JButton("Details");
        details.addActionListener(e -> showSelectedDetails());
        bottom.add(details);
        center.add(bottom, BorderLayout.SOUTH);

        return center;
    }

    private JComponent buildRightPanel() {
        RoundedPanel right = new RoundedPanel(14);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(12,12,12,12));
        right.setPreferredSize(new Dimension(260, 0));

        JLabel stats = new JLabel("Statistics");
        stats.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        right.add(stats);
        right.add(Box.createVerticalStrut(12));

        int total = repository.loadAll().size();
        JLabel totalLabel = new JLabel("Total reservations: " + total);
        totalLabel.setFont(ThemeManager.uiFont(14f, Font.PLAIN));
        right.add(totalLabel);

        right.add(Box.createVerticalStrut(8));
        JLabel fileLabel = new JLabel("Storage: " + repository.storageFilename());
        fileLabel.setFont(ThemeManager.uiFont(12f, Font.PLAIN));
        right.add(fileLabel);

        right.add(Box.createVerticalGlue());
        return right;
    }

    private void openBookDialog() {
        JDialog dlg = new JDialog(frame, "Book Ticket", true);
        dlg.setSize(480, 360);
        dlg.setLocationRelativeTo(frame);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setBorder(new EmptyBorder(12,12,12,12));
        form.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Passenger Name:"), g);
        g.gridx = 1;
        JTextField tfName = new JTextField(20);
        form.add(tfName, g);

        g.gridx = 0; g.gridy++;
        form.add(new JLabel("Age:"), g);
        g.gridx = 1;
        JTextField tfAge = new JTextField(6);
        form.add(tfAge, g);

        g.gridx = 0; g.gridy++;
        form.add(new JLabel("From Station:"), g);
        g.gridx = 1;
        JTextField tfFrom = new JTextField(20);
        form.add(tfFrom, g);

        g.gridx = 0; g.gridy++;
        form.add(new JLabel("To Station:"), g);
        g.gridx = 1;
        JTextField tfTo = new JTextField(20);
        form.add(tfTo, g);

        g.gridx = 0; g.gridy++;
        form.add(new JLabel("Train Name:"), g);
        g.gridx = 1;
        JTextField tfTrain = new JTextField(20);
        form.add(tfTrain, g);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submit = new JButton("Book");
        stylePrimary(submit);
        submit.addActionListener(e -> {
            String n = tfName.getText().trim();
            String a = tfAge.getText().trim();
            String f = tfFrom.getText().trim();
            String t = tfTo.getText().trim();
            String tr = tfTrain.getText().trim();
            if (n.isEmpty() || a.isEmpty() || f.isEmpty() || t.isEmpty() || tr.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int age;
            try { age = Integer.parseInt(a); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Enter a valid age.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Passenger p = new Passenger(n, age, f, t, tr);
            String pnr = service.createReservation(p);
            JOptionPane.showMessageDialog(dlg, "Booked. PNR: " + pnr, "Success", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose();
            refreshList();
        });

        foot.add(submit);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(foot, BorderLayout.SOUTH);
        ThemeManager.applyToComponentTree(dlg);
        dlg.setVisible(true);
    }

    private void openViewDialog() {
        String pnr = JOptionPane.showInputDialog(frame, "Enter PNR:", "View Reservation", JOptionPane.PLAIN_MESSAGE);
        if (pnr == null || pnr.trim().isEmpty()) return;
        Passenger p = service.getReservation(pnr.trim().toUpperCase());
        if (p == null) {
            JOptionPane.showMessageDialog(frame, "PNR not found.", "Not found", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextArea ta = new JTextArea(p.toString());
        ta.setEditable(false);
        ta.setBorder(new EmptyBorder(8,8,8,8));
        JScrollPane sc = new JScrollPane(ta);
        sc.setPreferredSize(new Dimension(520,220));
        JOptionPane.showMessageDialog(frame, sc, "Reservation " + pnr.toUpperCase(), JOptionPane.PLAIN_MESSAGE);
    }

    private void openCancelDialog() {
        String pnr = JOptionPane.showInputDialog(frame, "Enter PNR to cancel:", "Cancel Reservation", JOptionPane.PLAIN_MESSAGE);
        if (pnr == null || pnr.trim().isEmpty()) return;
        boolean ok = service.cancelReservation(pnr.trim().toUpperCase());
        if (ok) {
            JOptionPane.showMessageDialog(frame, "Cancelled successfully.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            refreshList();
        } else {
            JOptionPane.showMessageDialog(frame, "PNR not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSelectedDetails() {
        String sel = reservationList.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(frame, "Select a reservation from the list.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // list element format: PNR  —  Name  [from->to]
        String pnr = sel.split("\\s+")[0].trim();
        Passenger p = service.getReservation(pnr);
        if (p == null) {
            JOptionPane.showMessageDialog(frame, "Details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            refreshList();
            return;
        }
        JTextArea ta = new JTextArea(p.toString());
        ta.setEditable(false);
        JScrollPane sc = new JScrollPane(ta);
        sc.setPreferredSize(new Dimension(420,200));
        JOptionPane.showMessageDialog(frame, sc, "Details - " + pnr, JOptionPane.PLAIN_MESSAGE);
    }

    private void refreshList() {
        listModel.clear();
        Map<String, Passenger> all = repository.loadAll();
        List<String> keys = new ArrayList<>(all.keySet());
        Collections.sort(keys);
        for (String k : keys) {
            Passenger p = all.get(k);
            listModel.addElement(k + "  —  " + p.getName() + "  [" + p.getFrom() + "->" + p.getTo() + "]");
        }
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Reservations as CSV");
        int r = chooser.showSaveDialog(frame);
        if (r != JFileChooser.APPROVE_OPTION) return;
        File f = chooser.getSelectedFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println("PNR,Name,Age,From,To,Train");
            Map<String, Passenger> all = repository.loadAll();
            for (String k : new ArrayList<>(all.keySet())) {
                Passenger p = all.get(k);
                pw.printf("%s,%s,%d,%s,%s,%s%n",
                        k, p.getName().replace(",", " "), p.getAge(),
                        p.getFrom().replace(",", " "), p.getTo().replace(",", " "), p.getTrain().replace(",", " "));
            }
            JOptionPane.showMessageDialog(frame, "Exported to " + f.getAbsolutePath(), "Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Export failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stylePrimary(AbstractButton b) {
        b.setFont(ThemeManager.uiFont(13f, Font.BOLD));
        b.setBackground(new Color(26,115,232));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
    }
}
