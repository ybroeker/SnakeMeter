package snakemeter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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

    public static void main(String[] args) {
        Controller controller = new Controller();
    }

    /**
     * Window.
     */
    private final Window window;
    /**
     * Current selected mode.
     */
    private EMode mode = EMode.NONE;
    /**
     * FileChooser to open Image.
     */
    private final JFileChooser fc;
    /**
     * Model.
     */
    private final Model model;
    /**
     * Point, where a drag starts.
     */
    private Point dragStart = null;
    /**
     * Result-Format. 
     */
    private DecimalFormat decimalFormat = new DecimalFormat("###.#");

    public Controller() {
        model = new Model();
        window = new Window(this, model);
        this.enableButtons(false);

        fc = initFileChooser();

        initVersionCheck();
    }

    /**
     * Inits FileChooser.
     * <p>
     * @return FileChooser
     */
    public JFileChooser initFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new ImageFilter());

        fc.setAcceptAllFileFilterUsed(false);
        return fc;
    }

    /**
     * Inits and starts VersionCheck.
     * <p>
     * @return VersionCheck
     */
    public VersionCheck initVersionCheck() {
        VersionCheck versionCheck = new VersionCheck();

        boolean newerVersion = versionCheck.checkForNewerVersion();
        if (newerVersion) {
            versionCheck.addNewVersionHint(window);
        }
        return versionCheck;
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
                    window.getImagePanel().setLastDimension(null);
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
            window.getResult().setText("" + decimalFormat.format(model.calculate()) + " cm");
        } else if (e.getSource() == window.getUndo()) {
            model.undo();
            window.getImagePanel().repaint();
            window.getResult().setText("" + decimalFormat.format(model.calculate()) + " cm");
        } else if (e.getSource() == window.getRedo()) {
            model.redo();
            window.getImagePanel().repaint();
            window.getResult().setText("" + decimalFormat.format(model.calculate()) + " cm");
        } else if (e.getSource() == window.getReset()) {
            model.reset();
            window.getImagePanel().repaint();
            window.getResult().setText("" + 0 + " cm");
        }

    }

    /**
     * Enables Buttons for Scaling etc.
     * <p>
     * @param enable
     */
    private void enableButtons(boolean enable) {
        window.getScale1Button().setEnabled(enable);
        window.getMessButton().setEnabled(enable);
        window.getDragButton().setEnabled(enable);
    }

    /**
     * deselect all ToggleButtons.
     */
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
            window.getImagePanel().setCurserPoint(null);
            window.getImagePanel().setCurserScalePoint(null);
        } else if (mode == EMode.SCALE) {

            model.addScalePoint(e.getPoint());

        } else if (mode == EMode.POINT) {
            model.addPoint(e.getPoint());
        }
        window.getImagePanel().repaint();
        parseInput(window.getScale1Input().getText());
        window.getResult().setText("" + decimalFormat.format(model.calculate()) + " cm");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == EMode.DRAG && dragStart == null) {
            dragStart = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragStart = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        window.getImagePanel().setCurserPoint(null);
        window.getImagePanel().setCurserScalePoint(null);
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
        if (dragStart != null) {
            model.addDrag(dragStart, e.getPoint());
        }
        window.getImagePanel().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        window.getImagePanel().setCurserScalePoint(null);
        window.getImagePanel().setCurserPoint(null);

        switch (mode) {
            case SCALE:
                window.getImagePanel().setCurserScalePoint(e.getPoint());
                break;
            case POINT:
                window.getImagePanel().setCurserPoint(e.getPoint());
                break;
        }

        window.getImagePanel().repaint();
    }

}
