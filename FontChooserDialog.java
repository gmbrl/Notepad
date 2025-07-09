import javax.swing.*;
import java.awt.*;

public class FontChooserDialog extends JDialog {

    private Font selectedFont;
    private boolean confirmed = false;

    JComboBox<String> fontFamilyBox;
    JComboBox<Integer> fontSizeBox;
    JComboBox<String> fontStyleBox;

    public FontChooserDialog(JFrame parent, Font currentFont) {
        super(parent, "Choose Font", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        fontFamilyBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontStyleBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic", "Bold Italic"});
        fontSizeBox = new JComboBox<>(new Integer[]{10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40});

        fontFamilyBox.setSelectedItem(currentFont.getFamily());
        fontSizeBox.setSelectedItem(currentFont.getSize());
        fontStyleBox.setSelectedIndex(currentFont.getStyle());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Font:"));
        panel.add(fontFamilyBox);
        panel.add(new JLabel("Style:"));
        panel.add(fontStyleBox);
        panel.add(new JLabel("Size:"));
        panel.add(fontSizeBox);

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(_ -> {
            selectedFont = new Font(
                    (String) fontFamilyBox.getSelectedItem(),
                    fontStyleBox.getSelectedIndex(),
                    (Integer) fontSizeBox.getSelectedItem()
            );
            confirmed = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(okBtn);
        buttons.add(cancelBtn);

        add(panel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }

    public Font getSelectedFont() {
        return confirmed ? selectedFont : null;
    }
}
