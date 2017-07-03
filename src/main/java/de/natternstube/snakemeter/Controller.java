package de.natternstube.snakemeter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
     *
     * @return FileChooser
     */
    public JFileChooser initFileChooser() {
        final String[] formats = {"jpeg", "jpg", "gif", "png"};
        String desc = "Images (." + String.join(", .", formats) + ")";

        JFileChooser fc = new JFileChooser();

        fc.addChoosableFileFilter(new FileNameExtensionFilter(desc, formats));

        fc.setAcceptAllFileFilterUsed(false);
        return fc;
    }

    /**
     * Inits and starts VersionCheck.
     * <p>
     *
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
            int returnVal = fc.showOpenDialog(window.getFrame());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                try {
                    BufferedImage image = ImageIO.read(file);
                    model.setImage(image);
                    enableButtons(true);
                    model.reset();
                    window.getImagePanel().setLastDimension(null);
                    window.getFrame().repaint();
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
            window.getResult().setText(formatLength());
        } else if (e.getSource() == window.getUndo()) {
            model.undo();
            window.getImagePanel().repaint();
            window.getResult().setText(formatLength());
        } else if (e.getSource() == window.getRedo()) {
            model.redo();
            window.getImagePanel().repaint();
            window.getResult().setText(formatLength());
        } else if (e.getSource() == window.getReset()) {
            model.reset();
            window.getImagePanel().repaint();
            window.getResult().setText(formatLength());
        }
    }

    String formatLength() {
        return String.format("%s cm", decimalFormat.format(model.calculate()));
    }

    /**
     * Enables Buttons for Scaling etc.
     * <p>
     *
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
            model.addScalePoint(new Point(e.getPoint()).mul(1 / window.getImagePanel().getScale()));
        } else if (mode == EMode.POINT) {
            model.addPoint(new Point(e.getPoint()).mul(1 / window.getImagePanel().getScale()));
        }
        window.getImagePanel().repaint();
        parseInput(window.getScale1Input().getText());
        window.getResult().setText("" + decimalFormat.format(model.calculate()) + " cm");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == EMode.DRAG && dragStart == null) {
            dragStart = new Point(e.getPoint()).mul(1 / window.getImagePanel().getScale());
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
            model.addDrag(dragStart, new Point(e.getPoint()).mul(1 / window.getImagePanel().getScale()));
        }
        window.getImagePanel().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        window.getImagePanel().setCurserScalePoint(null);
        window.getImagePanel().setCurserPoint(null);

        switch (mode) {
            case SCALE:
                window.getImagePanel().setCurserScalePoint(new Point(e.getPoint()));
                break;
            case POINT:
                window.getImagePanel().setCurserPoint(new Point(e.getPoint()));
                break;
        }

        window.getImagePanel().repaint();
    }

}
