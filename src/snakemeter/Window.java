package snakemeter;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

/**
 *
 * @author yannick-broeker
 */
public class Window extends JFrame {

    public static final char SHORTCUT_UNDO = 'Z';
    public static final char SHORTCUT_RESET = 'R';
    public static final char SHORTCUT_OPEN = 'O';
    public static final char SHORTCUT_SAVE = 'S';
    public static final int SHIFT = java.awt.event.InputEvent.SHIFT_DOWN_MASK;
    public static final int CONTROL = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    private final Controller controller;


    private  JMenuItem load;
    private  JMenuItem save;
    private  JToggleButton messButton;
    private  JToggleButton scale1Button;
    private  JToggleButton dragButton;
    private  ImagePanel imagePanel;
    private  JTextField scale1Input;
    private  JLabel result;
    private  JMenuBar menu;
    private  JMenuItem reset;
    private  JMenuItem undo;
    private  JMenuItem redo;

    

    public Window(Controller controller, Model model) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.controller = controller;

        createMenu();

        this.imagePanel = new ImagePanel(model);
        this.add(imagePanel, CENTER);

        this.imagePanel.addMouseListener(controller);
        this.imagePanel.addMouseMotionListener(controller);

        this.setSize(1000, 800);
        this.setVisible(true);
    }

    private void createMenu() {
        menu = new JMenuBar();

        this.add(menu, PAGE_START);

        createFileMenu();
        
        createEditMenu();

        messButton = new JToggleButton("Messen");
        messButton.addActionListener(controller);
        menu.add(messButton);

        scale1Button = new JToggleButton("Maßstab setzten");
        scale1Button.addActionListener(controller);
        menu.add(scale1Button);

        
        dragButton = new JToggleButton("Drag");
        dragButton.addActionListener(controller);
        menu.add(dragButton);
        
        menu.add(new JSeparator(JSeparator.VERTICAL));

        menu.add(new JLabel("Maßstab:"));
        scale1Input = new JTextField("1");
        scale1Input.setColumns(10);
        scale1Input.setMaximumSize(new Dimension(100, 200));
        scale1Input.addActionListener(controller);
        menu.add(scale1Input);
        menu.add(new JLabel("cm"));

        //Platzhalter
        menu.add(new JLabel("   "));

        menu.add(new JLabel("Länge:"));
        result = new JLabel("0 cm");
        menu.add(result);

        menu.add(new JSeparator(JSeparator.VERTICAL));
    }

    private void createFileMenu() {
        JMenu jMenu = new JMenu("Datei");

        menu.add(jMenu);

        load = new JMenuItem("Bild laden");
        load.setAccelerator(KeyStroke.getKeyStroke(SHORTCUT_OPEN, CONTROL));
        jMenu.add(load);
        load.addActionListener(controller);
        
        
        save = new JMenuItem("Speichern");
        save.setAccelerator(KeyStroke.getKeyStroke(SHORTCUT_SAVE, CONTROL));
        jMenu.add(save);
        save.addActionListener(controller);
        save.setEnabled(false);//TODO
         
    }

    private void createEditMenu() {

        /*reset = new JButton("Reset");
         reset.addActionListener(controller);
         menu.add(reset);
         undo = new JButton("undo");
         undo.addActionListener(controller);
         menu.add(undo);
         redo = new JButton("redo");
         redo.addActionListener(controller);
         menu.add(redo);*/
        JMenu jMenu = new JMenu("Bearbeiten");

        reset = new JMenuItem("Reset");
        reset.addActionListener(controller);
        reset.setAccelerator(KeyStroke.getKeyStroke(SHORTCUT_RESET, CONTROL));
        jMenu.add(reset);
        undo = new JMenuItem("undo");
        undo.addActionListener(controller);
        undo.setAccelerator(KeyStroke.getKeyStroke(SHORTCUT_UNDO, CONTROL));
        jMenu.add(undo);
        redo = new JMenuItem("redo");
        redo.addActionListener(controller);
        redo.setAccelerator(KeyStroke.getKeyStroke(SHORTCUT_UNDO, SHIFT + CONTROL));
        jMenu.add(redo);

        menu.add(jMenu);
    }

    public AbstractButton getLoad() {
        return load;
    }

    public JToggleButton getMessButton() {
        return messButton;
    }

    public JToggleButton getScale1Button() {
        return scale1Button;
    }

    public Controller getController() {
        return controller;
    }

    public JTextField getScale1Input() {
        return scale1Input;
    }

    public JLabel getResult() {
        return result;
    }
    
    public void addNewVersionButton(JButton button) {
        menu.add(button);
        this.revalidate();
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    public AbstractButton getReset() {
        return reset;
    }

    public AbstractButton getUndo() {
        return undo;
    }

    public AbstractButton getRedo() {
        return redo;
    }

    public JMenuItem getSave() {
        return save;
    }

    public JToggleButton getDragButton() {
        return dragButton;
    }

}
