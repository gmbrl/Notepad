import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotePadApp extends JFrame implements ActionListener {

    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel statusBar;
    JFileChooser fileChooser;
    File currentFile = null;
    boolean darkMode = false;

    public NotePadApp() {
        setTitle("Advanced Notepad");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("San Francisco", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.DARK_GRAY);
        textArea.setCaretColor(Color.GRAY);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        createMenuBar();

        statusBar = new JLabel("Words: 0 | Characters: 0");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusBar, BorderLayout.SOUTH);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateStatus(); }
            public void removeUpdate(DocumentEvent e) { updateStatus(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
    }

    void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        addItem(file, "New");
        addItem(file, "Open");
        addItem(file, "Save");
        addItem(file, "Save As");
        file.addSeparator();
        addItem(file, "Exit");

        JMenu edit = new JMenu("Edit");
        addItem(edit, "Cut");
        addItem(edit, "Copy");
        addItem(edit, "Paste");
        addItem(edit, "Select All");
        addItem(edit, "Find & Replace");

        JMenu format = new JMenu("Format");
        JMenuItem fontItem = new JMenuItem("Font...");
        fontItem.addActionListener(e -> {
            FontChooserDialog dialog = new FontChooserDialog(this, textArea.getFont());
            Font selectedFont = dialog.getSelectedFont();
            if (selectedFont != null) textArea.setFont(selectedFont);
        });
        format.add(fontItem);

        JMenu view = new JMenu("View");
        addItem(view, "Toggle Mode");
        JMenuItem themeItem = new JMenuItem("Switch Theme");
        themeItem.addActionListener(e -> switchTheme());
        view.add(themeItem);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(format);
        menuBar.add(view);
        setJMenuBar(menuBar);
    }

    void addItem(JMenu menu, String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(this);
        menu.add(item);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "New" -> {
                textArea.setText("");
                currentFile = null;
                setTitle("Untitled - Notepad");
            }
            case "Open" -> openFile();
            case "Save" -> saveFile(false);
            case "Save As" -> saveFile(true);
            case "Exit" -> System.exit(0);
            case "Cut" -> textArea.cut();
            case "Copy" -> textArea.copy();
            case "Paste" -> textArea.paste();
            case "Select All" -> textArea.selectAll();
            case "Toggle Mode" -> toggleMode();
            case "Find & Replace" -> new FindReplaceDialog(this, textArea);
        }
    }

    void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
                setTitle(currentFile.getName() + " - Notepad");
            } catch (IOException ex) {
                showError("Unable to open file.");
            }
        }
    }

    void saveFile(boolean saveAs) {
        if (currentFile == null || saveAs) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                if (!currentFile.getName().endsWith(".txt")) {
                    currentFile = new File(currentFile.getAbsolutePath() + ".txt");
                }
            } else return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            textArea.write(writer);
            setTitle(currentFile.getName() + " - Notepad");
        } catch (IOException ex) {
            showError("Unable to save file.");
        }
    }

    void toggleMode() {
        darkMode = !darkMode;
        Color bg = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.DARK_GRAY;

        textArea.setBackground(bg);
        textArea.setForeground(fg);
        textArea.setCaretColor(fg);
        statusBar.setForeground(fg);
        statusBar.setBackground(bg);
    }

    void switchTheme() {
        try {
            UIManager.setLookAndFeel(darkMode ? new FlatMacLightLaf() : new FlatMacDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);
            darkMode = !darkMode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateStatus() {
        String text = textArea.getText();
        int wordCount = (text.trim().isEmpty()) ? 0 : text.trim().split("\\s+").length;
        int charCount = text.length();
        statusBar.setText("Words: " + wordCount + " | Characters: " + charCount);
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Notepad");
            }
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception e) {
            System.out.println("Look and feel not supported: " + e);
        }

        SwingUtilities.invokeLater(() -> new NotePadApp().setVisible(true));
    }
}
