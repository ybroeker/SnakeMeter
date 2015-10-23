package snakemeter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    private final Window window;

    private EMode mode = EMode.NONE;

    private final JFileChooser fc;

    private final Model model;
    
    private Point dragStart=null;

    public static void main(String[] args) {
        Controller controller = new Controller();
    }

    public void init() {
        enableButtons(false);
        window.getImagePanel().setModel(model);

        boolean newerVersion = new VersionCheck().checkForNewerVersion();
        if (newerVersion) {
            window.addNewerVersionHint();
        }

    }

//In response to a button click:
    public Controller() {
        window = new Window(this);

        model = new Model();

        fc = new JFileChooser();
        fc.addChoosableFileFilter(new ImageFilter());

        fc.setAcceptAllFileFilterUsed(false);

        this.init();
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
                    window.getImagePanel().lastDimension = null;
                    window.repaint();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

            }
        } else if (e.getSource() == (window.getScale1Button())) {
            mode = EMode.SCALE;
            deselectAll();
            window.getScale1Button().setSelected(true);
        } else if (e.getSource() == (window.getMessButton())) {
            mode = EMode.POINT;
            deselectAll();
            window.getMessButton().setSelected(true);
        } else if (e.getSource() == (window.getDragButton())) {
            mode = EMode.DRAG;
            deselectAll();
            window.getDragButton().setSelected(true);
        } else if (e.getSource() == (window.getScale1Input())) {
            parseInput(window.getScale1Input().getText());
            window.getResult().setText("" + model.calculate() + " cm");
        } else if (e.getSource() == window.getUndo()) {
            model.undo();
            window.getImagePanel().repaint();
            window.getResult().setText("" + model.calculate() + " cm");
        } else if (e.getSource() == window.getRedo()) {
            model.redo();
            window.getImagePanel().repaint();
            window.getResult().setText("" + model.calculate() + " cm");
        } else if (e.getSource() == window.getReset()) {
            model.reset();
            window.getImagePanel().repaint();
            window.getResult().setText("" + 0 + " cm");
        }

    }

    private void enableButtons(boolean enable) {
        window.getScale1Button().setEnabled(enable);
        window.getMessButton().setEnabled(enable);
        window.getDragButton().setEnabled(enable);
    }

    private void deselectAll() {
        window.getScale1Button().setSelected(false);
        window.getMessButton().setSelected(false);
        window.getDragButton().setSelected(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            mode = EMode.NONE;
            window.getScale1Button().setSelected(false);
            window.getMessButton().setSelected(false);
            window.getImagePanel().curserPoint = null;
            window.getImagePanel().curserScalePoint = null;
        } else if (mode == EMode.SCALE) {

            model.addScalePoint(e.getPoint());

        } else if (mode == EMode.POINT) {
            model.addPoint(e.getPoint());
        }
        window.getImagePanel().repaint();
        parseInput(window.getScale1Input().getText());
        window.getResult().setText("" + model.calculate() + " cm");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mode==EMode.DRAG && dragStart==null) {
            dragStart=e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragStart=null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        window.getImagePanel().curserPoint = null;
        window.getImagePanel().curserScalePoint = null;
        window.getImagePanel().repaint();
    }

    private void parseInput(String text) {
        float input = Float.parseFloat(text);
        if (input != Float.NaN) {
            model.setScale(Float.parseFloat(text));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStart!=null) {
            model.addDrag(dragStart, e.getPoint());
        }
        window.getImagePanel().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if (mode == EMode.SCALE) {
            window.getImagePanel().curserScalePoint = e.getPoint();
            window.getImagePanel().curserPoint = null;

        } else if (mode == EMode.POINT) {
            window.getImagePanel().curserPoint = e.getPoint();
            window.getImagePanel().curserScalePoint = null;

        } else {
            window.getImagePanel().curserScalePoint = null;
            window.getImagePanel().curserPoint = null;
        }
        window.getImagePanel().repaint();
    }
}
