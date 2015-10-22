package snakemeter;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author yannick-broeker
 */
public class Window extends JFrame {

    private final Controller controller;

    private final JFrame window;
    
    JButton load;
    JToggleButton messButton;
    JToggleButton massButton;
    ImagePanel imagePanel;
    JTextField inputField;
    JLabel result;
JPanel menuPanel;

    public Window(Controller controller) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
this.window=this;
        this.setLayout(new BorderLayout());

        this.controller = controller;
        this.controller.setWindow(this);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

        this.add(menuPanel, PAGE_START);

        load = new JButton("Bild laden");
        menuPanel.add(load);
        load.addActionListener(controller);

        messButton = new JToggleButton("Messen");
        messButton.addActionListener(controller);
        menuPanel.add(messButton);

        massButton = new JToggleButton("Maßstab setzten");
        massButton.addActionListener(controller);
        menuPanel.add(massButton);

        menuPanel.add(new JLabel("Maßstab:"));
        inputField = new JTextField("1");
        inputField.setColumns(10);
        inputField.setMaximumSize(new Dimension(100, 200));
        inputField.addActionListener(controller);
        menuPanel.add(inputField);
        menuPanel.add(new JLabel("cm"));
        
        //Platzhalter
        menuPanel.add(new JLabel("   "));
        
        menuPanel.add(new JLabel("Länge:"));
        result = new JLabel("0 cm");
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

    public void addNewerVersionHint() {
        JButton button = new JButton("Update verfügbar");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                
                panel.add(new JLabel("Eine neue Version ist unter"));
                
                JTextField textfield = new JTextField(VersionCheck.APP_PATH);
                textfield.setEditable(false);
                panel.add(textfield);
                
                panel.add(new JLabel("verfügbar"));
                
                JOptionPane.showMessageDialog(window, panel,"Update",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuPanel.add(button);
        this.revalidate();
    }
    
}
