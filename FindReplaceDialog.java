import javax.swing.*;
import java.awt.*;
public class FindReplaceDialog extends JDialog {

    private final JTextField findField = new JTextField(20);
    private final JTextField replaceField = new JTextField(20);
    private final JTextArea textArea;

    public FindReplaceDialog(JFrame parent, JTextArea textArea) {
        super(parent, "Find & Replace", false);
        this.textArea = textArea;

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Find:"));
        panel.add(findField);
        panel.add(new JLabel("Replace with:"));
        panel.add(replaceField);

        JButton findBtn = new JButton("Find");
        JButton replaceBtn = new JButton("Replace All");

        findBtn.addActionListener(e -> findText());
        replaceBtn.addActionListener(e -> replaceText());

        panel.add(findBtn);
        panel.add(replaceBtn);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    void findText() {
        String search = findField.getText();
        String content = textArea.getText();
        int index = content.indexOf(search);
        if (index >= 0) {
            textArea.setCaretPosition(index);
            textArea.select(index, index + search.length());
            textArea.grabFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Text not found.");
        }
    }

    void replaceText() {
        String find = findField.getText();
        String replace = replaceField.getText();
        String content = textArea.getText();
        if (find.isEmpty()) return;

        String newText = content.replaceAll(find, replace);
        textArea.setText(newText);
    }
}
