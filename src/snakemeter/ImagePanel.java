package snakemeter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author yannick-broeker
 */
public class ImagePanel extends JPanel {

    private BufferedImage resized;

    private Point curserPoint;
    private Point curserScalePoint;

    private Dimension lastDimension;
    private float lastScale = 0;
    private Model model;

    public static final Color COLOR_POINT = Color.GREEN;
    public static final Color COLOR_SCALE_1 = Color.RED;

    ImagePanel(Model model) {
        this.model=model;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (model!=null&&model.getImage() != null) {

            createAndResizeImage();

            g.drawImage(resized, 0, 0, null);

            drawScalePoints((Graphics2D) g);

            drawPoints((Graphics2D) g);

        }

    }

    public void setModel(Model model) {
        this.model = model;
    }

    private void drawPoints(Graphics2D graphics) {
        graphics.setColor(COLOR_POINT);

        if (curserPoint != null) {
            graphics.fillRect(curserPoint.x - 1, curserPoint.y - 1, 3, 3);
        }

        if (!model.getPoints().isEmpty()) {
            for (Point point : model.getPoints()) {
                graphics.fillRect(point.x - 1, point.y - 1, 3, 3);
            }

            if (model.getPoints().size() >= 2) {
                graphics.drawPolyline(model.getPointsX(), model.getPointY(), model.getPoints().size());
            }
            if (curserPoint != null) {
                graphics.drawLine(model.getPoints().getLast().x, model.getPoints().getLast().y, curserPoint.x, curserPoint.y);
            }

        }
    }

    private void drawScalePoints(Graphics2D graphics) {
        graphics.setColor(COLOR_SCALE_1);

        if (model.getCurrentScale() != null) {
            graphics.fillRect(model.getCurrentScale().x - 1, model.getCurrentScale().y - 1, 3, 3);
        }
        if (model.getLastScale() != null) {
            graphics.fillRect(model.getLastScale().x - 1, model.getLastScale().y - 1, 3, 3);
            graphics.drawLine(model.getCurrentScale().x, model.getCurrentScale().y, model.getLastScale().x, model.getLastScale().y);
        }
        if (curserScalePoint != null) {
            graphics.fillRect(curserScalePoint.x - 1, curserScalePoint.y - 1, 3, 3);
            if (model.getCurrentScale() != null) {
                graphics.drawLine(model.getCurrentScale().x, model.getCurrentScale().y, curserScalePoint.x, curserScalePoint.y);

            }
        }
    }

    private void createAndResizeImage() {
        if (lastDimension == null || this.getWidth() != lastDimension.getWidth() || this.getHeight() != lastDimension.getHeight()) {

            resized = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);

            float scaleX = (float) this.getWidth() / (float) model.getImage().getWidth();
            float scaleY = (float) this.getHeight() / (float) model.getImage().getHeight();

            float scale = scaleX < scaleY ? scaleX : scaleY;

            Image tmp = model.getImage().getScaledInstance((int) (model.getImage().getWidth() * scale), (int) (model.getImage().getHeight() * scale), Image.SCALE_SMOOTH);
            resized.getGraphics().drawImage(tmp, 0, 0, null);

            if (lastScale != 0) {
                if (model.getLastScale() != null) {
                    model.getLastScale().setLocation((float) model.getLastScale().x / lastScale * scale, (float) model.getLastScale().y / lastScale * scale);
                }
                if (model.getCurrentScale() != null) {
                    model.getCurrentScale().setLocation((float) model.getCurrentScale().x / lastScale * scale, (float) model.getCurrentScale().y / lastScale * scale);
                }
                for (Point point : model.getPoints()) {
                    point.setLocation((float) point.x / lastScale * scale, (float) point.y / lastScale * scale);
                }
            }

            lastDimension = new Dimension(this.getWidth(), this.getHeight());
            lastScale = scale;
        }
    }

    public void setCurserPoint(Point curserPoint) {
        this.curserPoint = curserPoint;
    }

    public void setCurserScalePoint(Point curserScalePoint) {
        this.curserScalePoint = curserScalePoint;
    }

    public void setLastDimension(Dimension lastDimension) {
        this.lastDimension = lastDimension;
    }

    public void setLastScale(float lastScale) {
        this.lastScale = lastScale;
    }
    
    
}
