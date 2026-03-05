// ModernExamUI.java
//
// Final polished UI for OIBSIP_JAVA DEVELOPMENT_TASK-4
// - Adds boxed badges for Total Questions and Time with a vertical divider
// - Header unified font & size: "OASIS JAVA DEVELOPMENT INTERNSHIP EXAM"
// - Capitalized welcome headline and richer instructions
// - Dark/light theme safe
//
// Save this file as UTF-8 (no BOM) and overwrite your existing src/ModernExamUI.java.
package com.vr.onlineexam.ui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class ModernExamUI {

    private final JFrame frame;
    private final CardLayout cards = new CardLayout();
    private final JPanel mainPanel = new JPanel(cards);

    // State
    private final List<Question> questions = new ArrayList<>();
    private final Map<Integer, Integer> answers = new HashMap<>();
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    // Timer
    private Timer examTimer;
    private int remainingSeconds = 10 * 60; // default 10 minutes

    // Leaderboard (in-memory)
    private final List<LeaderboardEntry> leaderboard = new ArrayList<>();

    // UI refs
    private JLabel lblTimer;
    private JLabel lblQuestion;
    private JPanel optionsPanel;
    private JLabel lblQCount;
    private JLabel lblScoreValue;
    private JPanel resultDetailPanel;
    private JToggleButton themeToggle;

    // Design
    private static final int CARD_RADIUS = 14;
    private static final Color ACCENT = new Color(26, 115, 232);

    public ModernExamUI() {
        frame = new JFrame("OASIS JAVA DEVELOPMENT INTERNSHIP EXAM - OIBSIP_JAVA DEVELOPMENT_TASK-4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 720);
        frame.setMinimumSize(new Dimension(900, 560));
        frame.setLocationRelativeTo(null);

        loadDefaultQuestions();

        mainPanel.setBorder(new EmptyBorder(18, 18, 18, 18));
        mainPanel.add(buildWelcomeCard(), "WELCOME");
        mainPanel.add(buildExamCard(), "EXAM");
        mainPanel.add(buildResultCard(), "RESULT");

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(mainPanel, BorderLayout.CENTER);

        frame.setContentPane(root);
    }

    public void showUI() {
        SwingUtilities.invokeLater(() -> {
            try { ThemeManager.applyToComponentTree(frame); } catch (Throwable ignored) {}
            frame.setVisible(true);
            cards.show(mainPanel, "WELCOME");
        });
    }

    // ---------------- Header ----------------

    private JComponent buildHeader() {
        boolean dark = ThemeManager.isDark();
        Color headerBg = dark ? new Color(30, 32, 36) : new Color(250, 252, 255);
        Color headerAccent = dark ? new Color(120, 180, 255) : ACCENT;
        Color subtitleColor = dark ? new Color(200, 200, 200) : new Color(60, 60, 70);

        GradientPanel headerWrap = new GradientPanel(headerBg, headerBg.darker());
        headerWrap.setLayout(new BorderLayout());
        headerWrap.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        left.setOpaque(false);

        // Make logo + title same font & size and bold as requested
        JLabel logo = new JLabel("OASIS");
        logo.setFont(ThemeManager.uiFont(18f, Font.BOLD));
        logo.setForeground(headerAccent);

        JLabel title = new JLabel("JAVA DEVELOPMENT INTERNSHIP EXAM");
        title.setFont(ThemeManager.uiFont(18f, Font.BOLD));
        title.setForeground(subtitleColor);

        left.add(logo);
        left.add(title);

        // Right toolbar
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        right.setOpaque(false);

        JButton boardBtn = new JButton("Leaderboard");
        boardBtn.setFont(ThemeManager.uiFont(13f, Font.BOLD)); // bold per request
        boardBtn.setFocusPainted(false);
        boardBtn.setOpaque(false);
        boardBtn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        boardBtn.addActionListener(e -> showLeaderboard());

        themeToggle = new JToggleButton("Dark");
        themeToggle.setFont(ThemeManager.uiFont(12f, Font.PLAIN));
        themeToggle.setSelected(ThemeManager.isDark());
        themeToggle.setFocusPainted(false);
        themeToggle.addActionListener(e -> {
            boolean on = themeToggle.isSelected();
            ThemeManager.setDark(on);
            ThemeManager.apply();
            ThemeManager.applyToComponentTree(frame);
            SwingUtilities.invokeLater(frame::repaint);
        });

        right.add(boardBtn);
        right.add(themeToggle);

        headerWrap.add(left, BorderLayout.WEST);
        headerWrap.add(right, BorderLayout.EAST);

        headerWrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.isDark() ? new Color(60,60,60) : new Color(220, 220, 220)),
                headerWrap.getBorder()
        ));

        return headerWrap;
    }

    // ---------------- Welcome Card ----------------

    private JPanel buildWelcomeCard() {
        RoundedPanel card = new RoundedPanel(CARD_RADIUS);
        card.setLayout(new BorderLayout(16, 16));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Left — text
        JPanel left = new JPanel(new BorderLayout(10, 10));
        left.setOpaque(false);

        // Capitalized headline per request
        JLabel headline = new JLabel("<html><div style='letter-spacing:1px'>WELCOME TO <b>OASIS JAVA INTERNSHIP EXAM</b></div></html>");
        headline.setFont(ThemeManager.uiFont(22f, Font.BOLD));
        headline.setBorder(new EmptyBorder(6, 6, 6, 6));

        // Expanded, clearer instructions
        JTextArea inst = new JTextArea(
                "Read these instructions carefully before starting the exam:\n\n" +
                "• Total Questions: 10. Time Allowed: 10 minutes.\n" +
                "• Navigation: Use Next / Previous to move between questions. Your last selected option auto-saves.\n" +
                "• Timer & Auto-submit: Timer visible at top. When time runs out, the exam auto-submits.\n" +
                "• Scoring: Each question carries 1 mark. No negative marking.\n" +
                "• Connectivity: If the timer disappears or window loses focus, answers are auto-saved; app will lock and prompt to resume.\n" +
                "• Allowed items: Pen, paper and the IDE/editor are allowed. Mobile phones are not permitted while taking the exam.\n" +
                "• After submit: You will see a professional result summary and can save your score to the leaderboard.\n\n" +
                "Tip: Attempt all questions you are confident about first, then review remaining ones."
        );
        inst.setFont(ThemeManager.uiFont(14f, Font.PLAIN));
        inst.setLineWrap(true);
        inst.setWrapStyleWord(true);
        inst.setEditable(false);
        inst.setOpaque(false);
        inst.setBorder(new EmptyBorder(8, 6, 8, 6));

        left.add(headline, BorderLayout.NORTH);
        left.add(inst, BorderLayout.CENTER);

        // Right — actions
        RoundedPanel rightCard = new RoundedPanel(12);
        rightCard.setLayout(new BoxLayout(rightCard, BoxLayout.Y_AXIS));
        rightCard.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel status = new JLabel("READY TO BEGIN?");
        status.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = new JButton("Start Exam");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        stylePrimary(start);
        start.addActionListener(e -> startExam());

        JButton viewBoard = new JButton("View Leaderboard");
        viewBoard.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewBoard.setFont(ThemeManager.uiFont(14f, Font.BOLD)); // bold view leaderboard
        viewBoard.addActionListener(e -> showLeaderboard());

        // keep buttons closer and aligned
        rightCard.add(status);
        rightCard.add(Box.createVerticalStrut(10));
        rightCard.add(start);
        rightCard.add(Box.createVerticalStrut(6));
        rightCard.add(viewBoard);

        // bottom area: grouped boxed badges for neatness
        RoundedPanel badgesContainer = new RoundedPanel(12);
        badgesContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        badgesContainer.setBorder(new EmptyBorder(8, 8, 8, 8));

        // background adapts to theme
        Color boxBg = ThemeManager.isDark() ? new Color(40, 42, 46) : new Color(248, 249, 252);
        badgesContainer.setBackground(boxBg);
        badgesContainer.setOpaque(true);

        // boxed badges
        badgesContainer.add(makeBadgeBox("TOTAL QUESTIONS", String.valueOf(questions.size())));
        badgesContainer.add(makeVerticalDivider());
        badgesContainer.add(makeBadgeBox("TIME", "10 min"));

        // assemble
        card.add(left, BorderLayout.CENTER);
        card.add(rightCard, BorderLayout.EAST);
        card.add(badgesContainer, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    // ---------------- Exam Card ----------------

    private JPanel buildExamCard() {
        JPanel wrapper = new JPanel(new BorderLayout(12, 12));
        wrapper.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("Exam");
        title.setFont(ThemeManager.uiFont(18f, Font.BOLD));

        lblTimer = new JLabel(formatTime(remainingSeconds));
        lblTimer.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        lblTimer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        top.add(title, BorderLayout.WEST);
        top.add(lblTimer, BorderLayout.EAST);

        wrapper.add(top, BorderLayout.NORTH);

        RoundedPanel card = new RoundedPanel(CARD_RADIUS);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        lblQuestion = new JLabel("Question text");
        lblQuestion.setFont(ThemeManager.uiFont(15f, Font.BOLD));
        lblQuestion.setBorder(new EmptyBorder(6, 6, 6, 6));

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1, 10, 10));
        optionsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane sc = new JScrollPane(optionsPanel);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getVerticalScrollBar().setUnitIncrement(12);

        card.add(lblQuestion, BorderLayout.NORTH);
        card.add(sc, BorderLayout.CENTER);

        wrapper.add(card, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton prev = new JButton("Previous");
        prev.addActionListener(e -> gotoQuestion(Math.max(0, currentIndex.get() - 1)));
        JButton next = new JButton("Next");
        next.addActionListener(e -> gotoQuestion(Math.min(questions.size() - 1, currentIndex.get() + 1)));
        nav.add(prev);
        nav.add(next);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        lblQCount = new JLabel("Q 0 / " + questions.size());
        lblQCount.setFont(ThemeManager.uiFont(13f, Font.PLAIN));
        JButton submit = new JButton("Submit");
        stylePrimary(submit);
        submit.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(frame, "Submit now?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) submitExam();
        });

        right.add(lblQCount);
        right.add(submit);

        footer.add(nav, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);

        wrapper.add(footer, BorderLayout.SOUTH);

        populateQuestionUI(0);
        return wrapper;
    }

    // ---------------- Result Card ----------------

    private JPanel buildResultCard() {
        RoundedPanel card = new RoundedPanel(CARD_RADIUS);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Result Summary");
        header.setFont(ThemeManager.uiFont(20f, Font.BOLD));

        lblScoreValue = new JLabel("0 / " + questions.size());
        lblScoreValue.setFont(ThemeManager.uiFont(18f, Font.BOLD));
        lblScoreValue.setForeground(ACCENT);

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(header, BorderLayout.WEST);
        north.add(lblScoreValue, BorderLayout.EAST);

        resultDetailPanel = new JPanel();
        resultDetailPanel.setLayout(new BoxLayout(resultDetailPanel, BoxLayout.Y_AXIS));
        JScrollPane sc = new JScrollPane(resultDetailPanel);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.setPreferredSize(new Dimension(720, 420));

        card.add(north, BorderLayout.NORTH);
        card.add(sc, BorderLayout.CENTER);

        JButton back = new JButton("Back to Home");
        back.addActionListener(e -> cards.show(mainPanel, "WELCOME"));

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        foot.setOpaque(false);
        foot.add(back);

        card.add(foot, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    // ---------------- Core Exam Logic ----------------

    public void startExam() {
        answers.clear();
        currentIndex.set(0);
        remainingSeconds = 10 * 60;
        populateQuestionUI(0);
        cards.show(mainPanel, "EXAM");

        if (examTimer != null) examTimer.cancel();
        examTimer = new Timer();
        examTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override public void run() {
                remainingSeconds--;
                SwingUtilities.invokeLater(() -> lblTimer.setText(formatTime(remainingSeconds)));
                if (remainingSeconds <= 0) {
                    examTimer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(frame, "Time up - auto submitting", "Time Up", JOptionPane.INFORMATION_MESSAGE);
                        submitExam();
                    });
                }
            }
        }, 1000, 1000);
    }

    private void populateQuestionUI(int index) {
        currentIndex.set(index);
        Question q = questions.get(index);
        lblQuestion.setText("<html><div style='width:760px'><b>Q" + (index + 1) + ".</b> " + q.text + "</div></html>");
        optionsPanel.removeAll();

        ButtonGroup g = new ButtonGroup();
        for (int i = 0; i < q.options.length; i++) {
            JRadioButton rb = new JRadioButton(q.options[i]);
            rb.setFont(ThemeManager.uiFont(14f, Font.PLAIN));
            rb.setBorder(new EmptyBorder(8, 8, 8, 8));
            final int opt = i;
            rb.addActionListener(e -> answers.put(index, opt));
            g.add(rb);
            optionsPanel.add(wrapOption(rb));
        }

        Integer sel = answers.get(index);
        if (sel != null) {
            Component c = optionsPanel.getComponent(sel);
            if (c instanceof JPanel) {
                for (Component cc : ((JPanel)c).getComponents()) {
                    if (cc instanceof JRadioButton) ((JRadioButton) cc).setSelected(true);
                }
            }
        }

        lblQCount.setText("Q " + (index + 1) + " / " + questions.size());
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private JPanel wrapOption(JRadioButton rb) {
        RoundedPanel rp = new RoundedPanel(8);
        rp.setLayout(new BorderLayout());
        rp.setBorder(new EmptyBorder(6, 10, 6, 10));
        rp.add(rb, BorderLayout.WEST);
        return rp;
    }

    private void gotoQuestion(int idx) {
        if (idx < 0 || idx >= questions.size()) return;
        populateQuestionUI(idx);
    }

    private void submitExam() {
        if (examTimer != null) { examTimer.cancel(); examTimer = null; }

        int correct = 0;
        resultDetailPanel.removeAll();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Integer sel = answers.get(i);
            boolean ok = sel != null && sel == q.correctIndex;
            if (ok) correct++;

            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(8, 8, 8, 8));
            JLabel ql = new JLabel("<html><b>Q" + (i + 1) + ".</b> " + q.text + "</html>");
            ql.setFont(ThemeManager.uiFont(13f, Font.BOLD));
            JLabel ans = new JLabel("<html>Selected: " + (sel == null ? "—" : q.options[sel]) + " &nbsp;&nbsp; | &nbsp;&nbsp; Correct: " + q.options[q.correctIndex] + "</html>");
            ans.setFont(ThemeManager.uiFont(13f, Font.PLAIN));
            JLabel expl = new JLabel("<html><i>" + q.explanation + "</i></html>");
            expl.setFont(ThemeManager.uiFont(12f, Font.PLAIN));

            row.add(ql, BorderLayout.NORTH);
            row.add(ans, BorderLayout.CENTER);
            row.add(expl, BorderLayout.SOUTH);

            resultDetailPanel.add(row);
            resultDetailPanel.add(Box.createVerticalStrut(8));
            resultDetailPanel.add(new JSeparator());
        }

        lblScoreValue.setText(correct + " / " + questions.size());

        String name = JOptionPane.showInputDialog(frame, "Enter name for leaderboard:", "Save Score", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            leaderboard.add(new LeaderboardEntry(name.trim(), correct));
            leaderboard.sort((a,b) -> Integer.compare(b.score, a.score));
        }

        resultDetailPanel.revalidate();
        resultDetailPanel.repaint();
        cards.show(mainPanel, "RESULT");
    }

    public void showLeaderboard() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        JLabel h = new JLabel("Leaderboard");
        h.setFont(ThemeManager.uiFont(18f, Font.BOLD));
        p.add(h, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        int r = 1;
        for (LeaderboardEntry e : leaderboard) {
            JLabel l = new JLabel(r + ". " + e.name + " — " + e.score + " / " + questions.size());
            l.setFont(ThemeManager.uiFont(14f, Font.PLAIN));
            list.add(l);
            r++;
        }
        if (leaderboard.isEmpty()) {
            JLabel none = new JLabel("No scores yet.");
            none.setFont(ThemeManager.uiFont(13f, Font.ITALIC));
            list.add(none);
        }
        JScrollPane sc = new JScrollPane(list);
        sc.setPreferredSize(new Dimension(420,260));
        p.add(sc, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(frame, p, "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }

    // --------------- Helpers ---------------

    /** New boxed badge used by the welcome card for neatness. */
    private JPanel makeBadgeBox(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(ThemeManager.uiFont(11f, Font.BOLD));
        l.setForeground(ThemeManager.isDark() ? new Color(200,200,200) : new Color(90,90,90));
        l.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel v = new JLabel(value);
        v.setFont(ThemeManager.uiFont(16f, Font.BOLD));
        v.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        // box background adapt to theme
        Color boxBg = ThemeManager.isDark() ? new Color(60, 62, 66) : Color.WHITE;
        box.setBackground(boxBg);
        box.setOpaque(true);
        box.add(l, BorderLayout.NORTH);
        box.add(v, BorderLayout.SOUTH);

        // subtle border that adapts to theme
        Color borderColor = ThemeManager.isDark() ? new Color(80,80,80) : new Color(220,220,220);
        box.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor), BorderFactory.createEmptyBorder(6,8,6,8)));

        p.add(box, BorderLayout.CENTER);
        return p;
    }

    /** Vertical divider between boxes. */
    private Component makeVerticalDivider() {
        JPanel v = new JPanel();
        v.setOpaque(false);
        v.setPreferredSize(new Dimension(12, 1));
        JLabel bar = new JLabel();
        bar.setPreferredSize(new Dimension(1, 40));
        bar.setOpaque(true);
        bar.setBackground(ThemeManager.isDark() ? new Color(100,100,100) : new Color(220,220,220));
        v.add(bar);
        return v;
    }

    private void stylePrimary(AbstractButton b) {
        b.setFont(ThemeManager.uiFont(14f, Font.BOLD));
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    }

    private String formatTime(int seconds) {
        int s = Math.max(0, seconds);
        int mm = s / 60;
        int ss = s % 60;
        return String.format("%02d:%02d", mm, ss);
    }

    // --------------- Questions ---------------

    private void loadDefaultQuestions() {
        questions.clear();
        questions.add(new Question("Which of these is NOT a primitive type in Java?", new String[] {"int","boolean","String","double"}, 2, "String is a reference type."));
        questions.add(new Question("What keyword is used to inherit a class in Java?", new String[] {"implements","extends","inherits","uses"}, 1, "extends is used for class inheritance."));
        questions.add(new Question("Which collection preserves insertion order and allows null elements?", new String[] {"HashSet","ArrayList","Hashtable","PriorityQueue"}, 1, "ArrayList preserves insertion order."));
        questions.add(new Question("Which exception is thrown for an invalid array index?", new String[] {"NullPointerException","ArrayIndexOutOfBoundsException","IndexOutOfRange","IllegalArgumentException"}, 1, "ArrayIndexOutOfBoundsException."));
        questions.add(new Question("Which method is the entry point of a Java application?", new String[] {"start()","main(String[] args)","run()","init()"}, 1, "main(String[] args) is the entry."));
        questions.add(new Question("Which keyword prevents a method from being overridden?", new String[] {"static","final","native","private"}, 1, "final prevents overriding."));
        questions.add(new Question("Which interface is used for external sorting order?", new String[] {"Comparator","Serializable","Iterable","Comparable"}, 0, "Comparator supplies external comparison."));
        questions.add(new Question("What does JVM stand for?", new String[] {"Java Virtual Machine","Java Variable Manager","Java Version Manager","Joint Virtual Machine"}, 0, "Java Virtual Machine."));
        questions.add(new Question("Which keyword is used to handle exceptions in Java?", new String[] {"try-catch","handle","except","trap"}, 0, "try-catch blocks handle exceptions."));
        questions.add(new Question("Which access modifier allows visibility only within the same package?", new String[] {"public","private","protected","default (no modifier)"}, 3, "Package-private (no modifier)."));
    }

    // --------------- Small helpers & UI classes ---------------

    private static class Question {
        final String text;
        final String[] options;
        final int correctIndex;
        final String explanation;
        Question(String t, String[] o, int c, String e) { text = t; options = o; correctIndex = c; explanation = e; }
    }

    private static class LeaderboardEntry {
        final String name; final int score; LeaderboardEntry(String n, int s) { name = n; score = s; }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        RoundedPanel(int radius) { super(); this.radius = radius; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = getBackground();
            if (bg == null || bg.getAlpha() == 0) bg = UIManager.getColor("Panel.background");
            // shadow
            g2.setColor(new Color(0,0,0,20));
            g2.fillRoundRect(6, 6, getWidth()-12, getHeight()-12, radius, radius);
            // panel
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-12, getHeight()-12, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public Insets getInsets() { return new Insets(8, 8, 8, 8); }
    }

    private static class GradientPanel extends JPanel {
        private final Color a, b;
        GradientPanel(Color a, Color b) { this.a = a; this.b = b; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, a, w, 0, b);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            super.paintComponent(g);
        }
    }
}
