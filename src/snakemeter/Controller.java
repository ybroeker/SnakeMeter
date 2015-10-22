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
    BufferedImage image;
    JFileChooser fc;

    LinkedList<Point> points = new LinkedList<>();

    Point point1;
    Point point2;

    float scale = 1;

    public void init() {
        enableButtons(false);
        imagePanel.points = points;
        
        
        boolean newerVersion = new VersionCheck().checkForNewerVersion();
        if (newerVersion) {
            window.addNewerVersionHint();
        }
        
    }

//In response to a button click:
    public Controller() {
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
                    image = ImageIO.read(file);
                    window.setImage(image);
                    enableButtons(true);
                    points.clear();

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
            calculate();
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
        }

        if (mode == MODE_SCALE) {

            if (point1 == null) {
                point1 = e.getPoint();
            } else {
                if (point2 != null) {
                    point1 = point2;
                }
                point2 = e.getPoint();
            }
            imagePanel.point2 = point2;
            imagePanel.point1 = point1;

        } else if (mode == MODE_MESS) {
            points.add(e.getPoint());
        }
        imagePanel.repaint();
        calculate();
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    public void setImagePanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    private void parseInput(String text) {
        scale = Float.parseFloat(text);
    }

    private void calculate() {
        parseInput(window.getInputField().getText());

        Point last = null;
        float lenghtPx = 0;
        float lenghtScale = 0;
        if (point1 != null && point2 != null) {
            lenghtScale += Point.distance(point1.x, point1.y, point2.x, point2.y);
        }

        for (Point point : points) {
            if (last != null) {
                lenghtPx += Point.distance(last.x, last.y, point.x, point.y);
            }
            last = point;
        }

        window.result.setText("" + lenghtPx / lenghtScale * scale+" cm");

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if (mode == MODE_SCALE) {
            imagePanel.pointDragScale = e.getPoint();
            imagePanel.pointDrag = null;

        } else if (mode == MODE_MESS) {
            imagePanel.pointDrag = e.getPoint();
            imagePanel.pointDragScale = null;

        } else {
            imagePanel.pointDragScale = null;
            imagePanel.pointDrag = null;

        }
        imagePanel.repaint();
    }
}
