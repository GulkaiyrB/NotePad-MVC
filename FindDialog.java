import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindDialog {
    private JDialog findDialog;

    public FindDialog(JFrame frame, JTextArea editorPane) {
        findDialog = new JDialog(frame);
        findDialog.setResizable(false);
        findDialog.setType(Type.UTILITY);
        findDialog.setTitle("Find");
        findDialog.setBounds(100, 100, 421, 214);
        findDialog.setLocationRelativeTo(frame);
        findDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        findDialog.getContentPane().setLayout(null);

        JPanel contentPanel = new JPanel();
        JButton findNextButton = new JButton("Find Next");
        JButton cancelButton = new JButton("Cancel");
        JTextField textField = new JTextField();

        //content panel
        contentPanel.setBounds(0, 0, 405, 175);
        findDialog.getContentPane().add(contentPanel);
        contentPanel.setLayout(null);

        //direction label
        JLabel directionLabel = new JLabel("Direction:");
        directionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        directionLabel.setBounds(10, 102, 62, 14);
        contentPanel.add(directionLabel);


        //radio buttons
        ButtonGroup bg = new ButtonGroup();

        JRadioButton upRadioButton = new JRadioButton("Up");
        bg.add(upRadioButton);
        upRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        upRadioButton.setBounds(82, 98, 41, 23);
        contentPanel.add(upRadioButton);

        JRadioButton downRadioButton = new JRadioButton("Down");
        bg.add(downRadioButton);
        downRadioButton.setSelected(true);
        downRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        downRadioButton.setBounds(126, 99, 57, 23);
        contentPanel.add(downRadioButton);

        //word wrap button
        JCheckBox wrapAroundButton = new JCheckBox("Wrap around");
        wrapAroundButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        wrapAroundButton.setBounds(6, 134, 117, 23);
        wrapAroundButton.setSelected(true);
        contentPanel.add(wrapAroundButton);

        //find next button
        findNextButton.setEnabled(false);
        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int currentposition = editorPane.getCaretPosition();
                String pattern = textField.getText();
                String editorText = editorPane.getText();

                Pattern p = Pattern.compile(pattern);
                //when wrap around button is selected :: round again and again if pattern is there in the editor text
                if (wrapAroundButton.isSelected()) {
                    //when upradio button is selected and wrap is selected
                    if (upRadioButton.isSelected()) {
                        try {
                            Matcher m = p.matcher(editorText);
                            if (!m.find()) {
                                JOptionPane.showMessageDialog(findDialog, "Cannot find '" + pattern + "'", "Notepad", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                int start = -1;
                                int end = 0;
                                boolean found = false;
                                if (editorPane.getSelectedText() != null) {
                                    currentposition = editorPane.getSelectionEnd() - 1;
                                }
                                while (m.find(start + 1)) {
                                    if (m.end() <= currentposition) {
                                        start = m.start();
                                        end = m.end();
                                        found = true;
                                    } else {
                                        break;
                                    }
                                }
                                if (!found) {
                                    //round again from last position of the editor text if not found above
                                    int start2 = -1;
                                    int end2 = 0;
                                    currentposition = editorText.length();
                                    while (m.find(start2 + 1)) {
                                        if (m.end() <= currentposition) {
                                            start2 = m.start();
                                            end2 = m.end();
                                        } else {
                                            break;
                                        }
                                    }
                                    editorPane.setCaretPosition(end2);
                                    editorPane.select(start2, end2);
                                } else {
                                    editorPane.setCaretPosition(end);
                                    editorPane.select(start, end);
                                }
                            }
                        } catch (Exception exp) {
                            JOptionPane.showMessageDialog(findDialog, "Error :find");
                        }
                        ////when upradio button is not selected and wrap is selected
                    } else {
                        try {
                            Matcher m = p.matcher(editorText);
                            if (!m.find()) {
                                JOptionPane.showMessageDialog(findDialog, "Cannot find '" + pattern + "'", "Notepad", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                m = p.matcher(editorText);
                                if (m.find(currentposition)) {
                                    editorPane.setCaretPosition(m.end());
                                    editorPane.select(m.start(), m.end());
                                } else {
                                    //if not find again search from starting position  (0)
                                    m = p.matcher(editorText);
                                    if (m.find(0)) {
                                        editorPane.setCaretPosition(m.end());
                                        editorPane.select(m.start(), m.end());
                                    }
                                }
                            }
                        } catch (Exception exp) {
                            JOptionPane.showMessageDialog(findDialog, "Error :find");
                        }
                    }
                    //when wrap button is not selected
                } else {
                    //when upradio button is selected and wrap not is selected
                    if (upRadioButton.isSelected()) {
                        try {
                            Matcher m = p.matcher(editorText);
                            int start = -1;
                            int end = 0;
                            boolean found = false;
                            if (editorPane.getSelectedText() != null) {
                                currentposition = editorPane.getSelectionEnd() - 1;
                            }
                            //find pattern as near to current position from start every time
                            while (m.find(start + 1)) {
                                if (m.end() <= currentposition) {
                                    start = m.start();
                                    end = m.end();
                                    found = true;
                                } else {
                                    break;
                                }
                            }
                            if (!found) {
                                JOptionPane.showMessageDialog(findDialog, "Cannot find '" + pattern + "'", "Notepad", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                editorPane.setCaretPosition(end);
                                editorPane.select(start, end);
                            }
                        } catch (Exception exp) {
                            JOptionPane.showMessageDialog(findDialog, "Error :find");
                        }
                        //when upradio button is not  selected and wrap not  is selected
                    } else {
                        try {
                            Matcher m = p.matcher(editorText);
                            if (m.find(currentposition)) {
                                editorPane.setCaretPosition(m.end());
                                editorPane.select(m.start(), m.end());
                            } else {
                                JOptionPane.showMessageDialog(findDialog, "Cannot find '" + pattern + "'", "Notepad", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception exp) {
                            JOptionPane.showMessageDialog(findDialog, "Error :find");
                        }
                    }
                }
            }
        });
        findNextButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        findNextButton.setBounds(306, 20, 89, 30);
        findDialog.getRootPane().setDefaultButton(findNextButton);
        contentPanel.add(findNextButton);

        //cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findDialog.dispose();
            }
        });
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        cancelButton.setBounds(306, 57, 89, 30);
        contentPanel.add(cancelButton);

        //text field
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField.getText() != null) {
                    findNextButton.setEnabled(true);
                } else {
                    findNextButton.setEnabled(false);
                }
            }
        });
        textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        textField.setBounds(82, 21, 220, 30);
        contentPanel.add(textField);
        textField.setColumns(10);

        JLabel textLabel = new JLabel("Find what:");
        textLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textLabel.setLabelFor(textField);
        textLabel.setBounds(10, 20, 62, 30);
        contentPanel.add(textLabel);

        findDialog.setModal(true);
        findDialog.setVisible(true);

    }
}

