package xyz.duncanruns.jingle.easycoop.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import xyz.duncanruns.jingle.easycoop.EasyCoop;
import xyz.duncanruns.jingle.easycoop.EasyCoopOptions;
import xyz.duncanruns.jingle.gui.JingleGUI;
import xyz.duncanruns.jingle.util.KeyboardUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusListener;
import java.util.Optional;
import java.util.regex.Pattern;


public class EasyCoopPanel {
    private static final Pattern NAME_PATTERN = Pattern.compile("^\\w+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[\\w@!$%^&*+#]*$");
    public JPanel mainPanel;
    private JButton startStopButton;
    private JButton copyIPButton;
    private JButton setMCPortButton;
    private JButton downloadButton;
    private JTextField addressField;
    private JButton launchButton;
    private JTextField nicknameField;
    private JTextField roomNameField;
    private JPasswordField roomPassField;
    private JPanel ninjaLinkLaunchPanel;
    private JCheckBox e4mcQabBox;
    private JCheckBox nlQabBox;

    private boolean ninjaLinkExists;
    private boolean ninjaLinkRunning = false;

    private boolean e4mcRunning = false;
    private String domain;

    public EasyCoopPanel() {
        EasyCoopOptions options = EasyCoop.options;
        $$$setupUI$$$();
        startStopButton.addActionListener(e -> onPressE4mcStart());
        copyIPButton.addActionListener(e -> {
            try {
                KeyboardUtil.copyToClipboard(domain);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        setMCPortButton.addActionListener(e -> {
            String errorMessage = "";
            while (true) {
                String input = Optional.ofNullable(JOptionPane.showInputDialog(this.mainPanel, errorMessage + "Enter your Minecraft server's port:", "e4mcbiat: change port", JOptionPane.PLAIN_MESSAGE, null, null, Integer.toString(options.e4mcbiatPort))).map(Object::toString).orElse(null);
                if (input == null || input.trim().isEmpty()) return;
                try {
                    int port = Integer.parseInt(input.trim());
                    if (port >= 1 && port <= 65535) {
                        EasyCoop.setMCPort(port);
                        return;
                    } else {
                        errorMessage = "Port must be between 1 and 65535!\n";
                    }
                } catch (NumberFormatException ex) {
                    errorMessage = "Invalid number format. Please enter a valid port.\n";
                }
            }
        });
        downloadButton.addActionListener(e -> {
            downloadButton.setEnabled(false);
            downloadButton.setText("Downloading...");
            ninjaLinkLaunchPanel.setVisible(false);
            Thread thread = new Thread(EasyCoop::downloadNinjaLink);
            thread.setDaemon(true);
            thread.start();

        });
        downloadButton.setBackground(new Color(0, 128, 0));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setVisible(false);
        ninjaLinkExists = !options.nlJar.isEmpty();
        downloadButton.setText(!ninjaLinkExists ? "Download" : "Update");
        ninjaLinkLaunchPanel.setVisible(ninjaLinkExists);

        addressField.setText(options.nlAddress);
        ((FocusListener) addressField).focusLost(null);
        nicknameField.setText(options.nlNickname);
        roomNameField.setText(options.nlRoomName);
        roomPassField.setText(options.nlRoomPass);

        launchButton.addActionListener(e -> onPressNlLaunch());
        updateLaunchButton();


        for (JTextField field : new JTextField[]{addressField, nicknameField, roomNameField, roomPassField}) {
            field.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateLaunchButton();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateLaunchButton();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateLaunchButton();
                }
            });
        }

        e4mcQabBox.setSelected(options.e4mcQAB);
        nlQabBox.setSelected(options.nlQAB);
        e4mcQabBox.addActionListener(e -> {
            options.e4mcQAB = e4mcQabBox.isSelected();
            JingleGUI.get().refreshQuickActions();
        });
        nlQabBox.addActionListener(e -> {
            options.nlQAB = nlQabBox.isSelected();
            JingleGUI.get().refreshQuickActions();
        });
    }

    public void onPressNlLaunch() {
        EasyCoopOptions options = EasyCoop.options;
        options.nlAddress = addressField.getText().trim();
        if (options.nlAddress.equals(EasyCoop.DEFAULT_NL_ADDRESS)) options.nlAddress = "";
        options.nlNickname = nicknameField.getText().trim();
        options.nlRoomName = roomNameField.getText().trim();
        options.nlRoomPass = new String(roomPassField.getPassword()).trim();
        ninjaLinkRunning = EasyCoop.launchNinjaLink();
        updateLaunchButton();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 3, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setText("e4mc");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startStopButton = new JButton();
        startStopButton.setText("Start");
        panel1.add(startStopButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyIPButton = new JButton();
        copyIPButton.setEnabled(false);
        copyIPButton.setText("Copy IP");
        panel1.add(copyIPButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setMCPortButton = new JButton();
        setMCPortButton.setText("Set MC Port");
        panel1.add(setMCPortButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        e4mcQabBox = new JCheckBox();
        e4mcQabBox.setText("Quick Action Button");
        panel1.add(e4mcQabBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        mainPanel.add(separator1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("NinjaLink");
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ninjaLinkLaunchPanel = new JPanel();
        ninjaLinkLaunchPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        ninjaLinkLaunchPanel.setEnabled(true);
        panel2.add(ninjaLinkLaunchPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Address:");
        ninjaLinkLaunchPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Nickname:");
        ninjaLinkLaunchPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Room Name:");
        ninjaLinkLaunchPanel.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Room Password:");
        ninjaLinkLaunchPanel.add(label6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ninjaLinkLaunchPanel.add(addressField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        launchButton = new JButton();
        launchButton.setText("Launch");
        ninjaLinkLaunchPanel.add(launchButton, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nicknameField = new JTextField();
        ninjaLinkLaunchPanel.add(nicknameField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        roomNameField = new JTextField();
        ninjaLinkLaunchPanel.add(roomNameField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        roomPassField = new JPasswordField();
        ninjaLinkLaunchPanel.add(roomPassField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        nlQabBox = new JCheckBox();
        nlQabBox.setText("Quick Action Button");
        ninjaLinkLaunchPanel.add(nlQabBox, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadButton = new JButton();
        downloadButton.setText("Download");
        panel2.add(downloadButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public void onE4mcStarted(String domain) {
        this.e4mcRunning = true;
        this.domain = domain;
        copyIPButton.setEnabled(true);
        startStopButton.setEnabled(true);
        startStopButton.setText("Stop");
    }

    public void onE4mcStopped() {
        this.e4mcRunning = false;
        copyIPButton.setEnabled(false);
        startStopButton.setEnabled(true);
        startStopButton.setText("Start");
    }

    public void onUpdateAvailable() {
        downloadButton.setText(ninjaLinkExists ? "Update" : "Download");
        downloadButton.setVisible(true);
        downloadButton.setEnabled(true);
    }

    public void onFinishNLUpdate() {
        ninjaLinkExists = true;
        downloadButton.setVisible(false);
        ninjaLinkLaunchPanel.setVisible(true);
        updateLaunchButton();
    }

    public void onNinjaLinkClosed() {
        ninjaLinkRunning = false;
        updateLaunchButton();
    }

    private void updateLaunchButton() {
        launchButton.setEnabled(ninjaLinkExists && !ninjaLinkRunning && nlFieldsValid());
    }

    private boolean nlFieldsValid() {
        String nickname = nicknameField.getText();
        String roomName = roomNameField.getText();
        String roomPass = new String(roomPassField.getPassword());
        if (nickname != null && !NAME_PATTERN.matcher(nickname).matches()) return false;
        if (roomName.isEmpty()) return roomPass.isEmpty();
        if (!NAME_PATTERN.matcher(roomName).matches()) return false;
        return PASSWORD_PATTERN.matcher(roomPass).matches();
    }

    public void onPressE4mcStart() {
        startStopButton.setEnabled(false);
        if (!e4mcRunning) {
            EasyCoop.startE4mc();
        } else {
            EasyCoop.stopE4mc();
        }
    }

    private void createUIComponents() {
        addressField = new HintTextField(EasyCoop.DEFAULT_NL_ADDRESS);
    }
}
