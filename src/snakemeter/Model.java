package snakemeter;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import snakemeter.Action.Action;
import snakemeter.Action.EAction;

/**
 *
 * @author yannick-broeker
 */
public class Model {

    LinkedList<Action> actions = new LinkedList<>();
    LinkedList<Action> actionsToDo = new LinkedList<>();

    BufferedImage image;

    LinkedList<Point> points = new LinkedList<>();

    LinkedList<Point> scalePoints = new LinkedList<>();

    float scale = 1;

    public float calculate() {
        if (!(scalePoints.size() >= 2) || !(points.size() >= 2)) {
            //no scale...
            return 0;
        }

        float lenghtScale = 0;
        lenghtScale += Point.distance(
                scalePoints.get(scalePoints.size() - 1).x, scalePoints.get(scalePoints.size() - 1).y,
                scalePoints.get(scalePoints.size() - 2).x, scalePoints.get(scalePoints.size() - 2).y);

        Point last = null;
        float lenghtPx = 0;
        for (Point point : points) {
            if (last != null) {
                lenghtPx += Point.distance(last.x, last.y, point.x, point.y);
            }
            last = point;
        }

        return lenghtPx / lenghtScale * scale;
    }

    public Point getLastScale() {
        if (scalePoints.size() >= 2) {
            return scalePoints.get(scalePoints.size() - 2);
        }
        return null;
    }

    public Point getCurrentScale() {
        if (!scalePoints.isEmpty()) {
            return scalePoints.getLast();
        }
        return null;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void addScalePoint(Point point) {
        actionsToDo.clear();
        actions.add(new Action(EAction.SCALE, point));
        scalePoints.add(point);
    }

    public void addPoint(Point point) {
        actionsToDo.clear();
        actions.add(new Action(EAction.POINT, point));
        points.add(point);
    }

    public void reset() {
        Collections.reverse(actions);
        actionsToDo.addAll(actions);
        actions.clear();
        points.clear();
        scalePoints.clear();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public LinkedList<Point> getPoints() {
        return points;
    }

    public LinkedList<Point> getScalePoints() {
        return scalePoints;
    }

    public BufferedImage getImage() {
        return image;
    }

    public float getScale() {
        return scale;
    }

    public int[] getPointsX() {
        int[] pointsX = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            pointsX[i] = points.get(i).x;
        }
        return pointsX;
    }

    public int[] getPointY() {
        int[] pointsY = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            pointsY[i] = points.get(i).y;
        }
        return pointsY;
    }

    public void redo() {
        if (!actionsToDo.isEmpty()) {
            Action action = actionsToDo.removeLast();

            actions.add(action);

            if (action.getAction() == EAction.POINT) {

                points.add(action.getPoint());

            } else if (action.getAction() == EAction.SCALE) {
                scalePoints.add(action.getPoint());

            }

        }
    }

    public void undo() {
        if (!actions.isEmpty()) {
            Action action = actions.removeLast();

            actionsToDo.add(action);

            if (action.getAction() == EAction.POINT) {
                if (!points.isEmpty()) {
                    points.removeLast();
                }

            } else if (action.getAction() == EAction.SCALE) {
                if (!scalePoints.isEmpty()) {
                    scalePoints.removeLast();
                }

            }

        }
    }

}
