package snakemeter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author yannick-broeker
 */
public class ImagePanel extends JPanel {

    BufferedImage image;

    BufferedImage resized;

    LinkedList<Point> points;

    Point point1;
    Point point2;
    
    Point pointDrag;
    Point pointDragScale;

    Dimension lastDimension;
    float lastScale = 0;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (image != null) {

            if (lastDimension == null || this.getWidth() != lastDimension.getWidth() || this.getHeight() != lastDimension.getHeight()) {
                
                
                resized = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);

                float scaleX = (float) this.getWidth() / (float) image.getWidth();
                float scaleY = (float) this.getHeight() / (float) image.getHeight();

                float scale = scaleX < scaleY ? scaleX : scaleY;

                Image tmp = image.getScaledInstance((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), Image.SCALE_SMOOTH);
                resized.getGraphics().drawImage(tmp, 0, 0, null);

                if (lastScale!=0) {
                    if (point1!=null) {
                        point1.setLocation((float)point1.x/lastScale*scale, (float)point1.y/lastScale*scale);
                    }
                    if (point2!=null) {
                        point2.setLocation((float)point2.x/lastScale*scale, (float)point2.y/lastScale*scale);
                    }
                    for (Point point : points) {
                        point.setLocation((float)point.x/lastScale*scale, (float)point.y/lastScale*scale);
                    }
                }                
                
                lastDimension = new Dimension(this.getWidth(), this.getHeight());
                lastScale=scale;
            }

            g.drawImage(resized, 0, 0, null); // see javadoc for more info on the parameters     

            g.setColor(Color.red);
            if (point1 != null) {
                g.fillRect(point1.x - 1, point1.y - 1, 3, 3);
            }
            if (point2 != null) {
                g.fillRect(point2.x - 1, point2.y - 1, 3, 3);
                g.drawLine(point1.x, point1.y, point2.x, point2.y);
            }
            if (pointDragScale!=null) {
                g.fillRect(pointDragScale.x - 1, pointDragScale.y - 1, 3, 3);
                if (point1!=null) {
                    g.drawLine(point1.x, point1.y, pointDragScale.x, pointDragScale.y);
   
                }
            }
            
            

            g.setColor(Color.green);
            Point last = null;
            if (points != null) {
                for (Point point : points) {

                    g.fillRect(point.x - 1, point.y - 1, 3, 3);

                    if (last != null) {
                        g.drawLine(point.x, point.y, last.x, last.y);
                    }

                    last = point;
                }
            }
            
            if (pointDrag!=null) {
                g.fillRect(pointDrag.x - 1, pointDrag.y - 1, 3, 3);
                if (!points.isEmpty()) {
                    g.drawLine(points.getLast().x, points.getLast().y, pointDrag.x, pointDrag.y);
   
                }
            }

        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        resized = null; 
        lastDimension = null; 
    }

}
