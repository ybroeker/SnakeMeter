package snakemeter;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author yannick-broeker
 */
public class Window extends JFrame {

    private final Controller controller;

    JButton load;
    JToggleButton messButton;
    JToggleButton massButton;
    ImagePanel imagePanel;
    JTextField inputField;
    JLabel result;

    public Window(Controller controller) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        this.controller = controller;
        this.controller.setWindow(this);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

        this.add(menuPanel, PAGE_START);

        load = new JButton("load");
        menuPanel.add(load);
        load.addActionListener(controller);

        messButton = new JToggleButton("messen");
        messButton.addActionListener(controller);
        menuPanel.add(messButton);

        massButton = new JToggleButton("maßstab");
        massButton.addActionListener(controller);
        menuPanel.add(massButton);

        inputField = new JTextField("1");
        inputField.setColumns(10);
        inputField.addActionListener(controller);
        menuPanel.add(inputField);

        result = new JLabel("0cm");
        menuPanel.add(result);

        this.imagePanel = new ImagePanel();
        this.add(imagePanel, CENTER);

        this.controller.setImagePanel(imagePanel);
        this.imagePanel.addMouseListener(controller);
        this.imagePanel.addMouseMotionListener(controller);

        this.setSize(800, 600);
        this.setVisible(true);
        this.controller.init();
    }

    public JButton getLoad() {
        return load;
    }

    public JToggleButton getMessButton() {
        return messButton;
    }

    public JToggleButton getMassButton() {
        return massButton;
    }

    public Controller getController() {
        return controller;
    }

    public JPanel getImagePanel() {
        return imagePanel;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public JLabel getResult() {
        return result;
    }

    public void setImage(BufferedImage image) {
        this.imagePanel.setImage(image);
        this.repaint();
    }

}
