package snakemeter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author yannick-broeker
 */
public class Controller implements ActionListener, MouseInputListener {

    private Window window;
    private ImagePanel imagePanel;

    public static final int MODE_NONE = 0, MODE_SCALE = 1, MODE_MESS = 2;
    private int mode = MODE_NONE;
    
    JFileChooser fc;

    Model model;

    public void init() {
        enableButtons(false);
        imagePanel.setModel(model);

        boolean newerVersion = new VersionCheck().checkForNewerVersion();
        if (newerVersion) {
            window.addNewerVersionHint();
        }

    }

//In response to a button click:
    public Controller() {
        model = new Model();

        fc = new JFileChooser();
        fc.addChoosableFileFilter(new ImageFilter());

        fc.setAcceptAllFileFilterUsed(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == (window.getLoad())) {
            //LOAD
            int returnVal = fc.showOpenDialog(window);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                try {
                    BufferedImage image = ImageIO.read(file);
                    model.setImage(image);
                    enableButtons(true);
                    model.reset();
                    window.getImagePanel().lastDimension=null;

                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

            }
        } else if (e.getSource() == (window.getMassButton())) {
            mode = MODE_SCALE;
            window.getMessButton().setSelected(false);

        } else if (e.getSource() == (window.getMessButton())) {
            mode = MODE_MESS;
            window.getMassButton().setSelected(false);
            
        } else if (e.getSource() == (window.getInputField())) {
            parseInput(window.getInputField().getText());
            window.result.setText("" + model.calculate() + " cm");
        } else  if (e.getSource() == window.getUndo()) {
            model.undo();
            imagePanel.repaint();
            window.result.setText("" + model.calculate() + " cm");
        } else if (e.getSource() == window.getRedo()) {
            model.redo();
            imagePanel.repaint();
            window.result.setText("" + model.calculate() + " cm");
        } else if (e.getSource() == window.getReset()) {
            model.reset();
            imagePanel.repaint();
            window.result.setText("" + 0 + " cm");
        }
        
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    private void enableButtons(boolean enable) {
        window.inputField.setEnabled(enable);
        window.massButton.setEnabled(enable);
        window.messButton.setEnabled(enable);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            mode = MODE_NONE;
            window.getMassButton().setSelected(false);
            window.getMessButton().setSelected(false);
            imagePanel.curserPoint=null;
            imagePanel.curserScalePoint=null;
        } else if (mode == MODE_SCALE) {

           model.addScalePoint(e.getPoint());

        } else if (mode == MODE_MESS) {
            model.addPoint(e.getPoint());
        }
        imagePanel.repaint();
        parseInput(window.getInputField().getText());
        window.result.setText("" + model.calculate() + " cm");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        imagePanel.curserPoint=null;
            imagePanel.curserScalePoint=null;
            imagePanel.repaint();
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    public void setImagePanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    private void parseInput(String text) {
        float input = Float.parseFloat(text);
        if (input != Float.NaN) {
            model.setScale(Float.parseFloat(text));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if (mode == MODE_SCALE) {
            imagePanel.curserScalePoint = e.getPoint();
            imagePanel.curserPoint = null;

        } else if (mode == MODE_MESS) {
            imagePanel.curserPoint = e.getPoint();
            imagePanel.curserScalePoint = null;

        } else {
            imagePanel.curserScalePoint = null;
            imagePanel.curserPoint = null;

        }
        imagePanel.repaint();
    }
}
