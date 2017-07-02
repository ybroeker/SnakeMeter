package de.natternstube.snakemeter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author yannick-broeker
 */
public class ImagePanel extends JPanel {

    private boolean showBezier = true;

    private BufferedImage resized;

    private Point curserPoint;
    private Point curserScalePoint;

    private Dimension lastDimension;
    private float lastScale = 0;
    private Model model;

    public static final Color COLOR_POINT = Color.GREEN;
    public static final Color COLOR_SCALE_1 = Color.RED;

    ImagePanel(Model model) {
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (model != null && model.getImage() != null) {

            createAndResizeImageIfNecessary();

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
            graphics.fillRect(curserPoint.toAWT().x - 1, curserPoint.toAWT().y - 1, 3, 3);
        }

        if (!model.getPoints().isEmpty()) {
            List<Point> points = showBezier ? model.bezier() : model.getPoints();

            int[] xs = new int[points.size()];
            int[] ys = new int[points.size()];
            int i = 0;
            for (Point point : points) {
                xs[i] = point.mul(lastScale).toAWT().x;
                ys[i] = point.mul(lastScale).toAWT().y;
                graphics.fillRect(xs[i] - 1, ys[i] - 1, 3, 3);
                i++;
            }

            if (model.getPoints().size() >= 2) {
                graphics.drawPolyline(xs, ys, xs.length);
            }
            if (curserPoint != null) {
                graphics.drawLine(xs[xs.length - 1], ys[ys.length - 1], curserPoint.toAWT().x, curserPoint.toAWT().y);
            }


        }
    }

    private void drawScalePoints(Graphics2D graphics) {
        graphics.setColor(COLOR_SCALE_1);

        if (model.getCurrentScale() != null) {
            graphics.fillRect(model.getCurrentScale().mul(lastScale).toAWT().x - 1, model.getCurrentScale().mul(lastScale).toAWT().y - 1, 3, 3);
        }
        if (model.getLastScale() != null) {
            graphics.fillRect(model.getLastScale().mul(lastScale).toAWT().x - 1, model.getLastScale().mul(lastScale).toAWT().y - 1, 3, 3);
            graphics.drawLine(model.getCurrentScale().mul(lastScale).toAWT().x, model.getCurrentScale().mul(lastScale).toAWT().y, model.getLastScale().mul(lastScale).toAWT().x, model.getLastScale().mul(lastScale).toAWT().y);
        }
        if (curserScalePoint != null) {
            graphics.fillRect(curserScalePoint.toAWT().x - 1, curserScalePoint.toAWT().y - 1, 3, 3);
            if (model.getCurrentScale() != null) {
                graphics.drawLine(model.getCurrentScale().mul(lastScale).toAWT().x, model.getCurrentScale().mul(lastScale).toAWT().y, curserScalePoint.toAWT().x, curserScalePoint.toAWT().y);

            }
        }
    }

    private void createAndResizeImageIfNecessary() {
        if (lastDimension == null || this.getWidth() != lastDimension.getWidth() || this.getHeight() != lastDimension.getHeight()) {

            resized = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);

            float scaleX = (float) this.getWidth() / (float) model.getImage().getWidth();
            float scaleY = (float) this.getHeight() / (float) model.getImage().getHeight();

            float scale = scaleX < scaleY ? scaleX : scaleY;

            Image tmp = model.getImage().getScaledInstance((int) (model.getImage().getWidth() * scale), (int) (model.getImage().getHeight() * scale), Image.SCALE_SMOOTH);
            resized.getGraphics().drawImage(tmp, 0, 0, null);

            /*
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
            */

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


    public double getScale() {
        return lastScale;
    }
}
