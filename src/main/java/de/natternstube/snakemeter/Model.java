package de.natternstube.snakemeter;

import de.natternstube.snakemeter.action.Action;
import de.natternstube.snakemeter.action.DragAction;

import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yannick-broeker
 */
public class Model {

    Deque<Action> actions = new LinkedList<>();
    Deque<Action> actionsToDo = new LinkedList<>();

    private BufferedImage image;

    private LinkedList<Point> points = new LinkedList<>();

    private LinkedList<Point> scalePoints = new LinkedList<>();

    /**
     * Real length of one pixel.
     */
    private float scale = 1;

    /**
     * Calculates the length of the curve.
     *
     * @param bezier if the bezier-curve should be used
     * @return the length of the curve
     */
    public double calculate(boolean bezier) {
        if (!(scalePoints.size() >= 2) || !(points.size() >= 2)) {
            //no scale...
            return 0;
        }

        double lenghtScale = scalePoints.get(scalePoints.size() - 1).distance(scalePoints.get(scalePoints.size() - 2));

        Point last = null;
        float lenghtPx = 0;
        for (Point point : (bezier ? bezier() : points)) {
            if (last != null) {
                lenghtPx += last.distance(point);
            }
            last = point;
        }

        return lenghtPx / lenghtScale * scale;
    }

    /**
     * Calculates the length of the curve, using the non-bezier-curve. {@link #calculate()}
     *
     * @return the length of the curve
     */
    public double calculate() {
        return calculate(false);
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
        actions.add(new Action(EMode.SCALE, point));
        scalePoints.add(point);
    }

    public void addPoint(Point point) {
        actionsToDo.clear();
        actions.add(new Action(EMode.POINT, point));
        points.add(point);
    }

    public void addDrag(Point from, Point to) {
        actionsToDo.clear();

        for (int i = 0; i < points.size(); i++) {
            if (isSamePoint(points.get(i), from)) {

                DragAction drag = new DragAction(i, from, to);
                actions.add(drag);

                points.set(i, to);
                break;
            }
        }
    }

    public void reset() {
        while (!actions.isEmpty()) {
            actionsToDo.push(actions.pop());
        }
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

    public void redo() {
        if (!actionsToDo.isEmpty()) {
            Action action = actionsToDo.removeLast();

            actions.add(action);

            if (action.getAction() == EMode.POINT) {

                points.add(action.getPoint());

            } else if (action.getAction() == EMode.SCALE) {
                scalePoints.add(action.getPoint());

            } else if (action.getAction() == EMode.DRAG) {
                DragAction drag = ((DragAction) action);

                points.set(drag.getIndex(), drag.getPoint());
            }

        }
    }

    public void undo() {
        if (!actions.isEmpty()) {
            Action action = actions.removeLast();

            actionsToDo.add(action);

            if (action.getAction() == EMode.POINT) {
                if (!points.isEmpty()) {
                    points.removeLast();
                }

            } else if (action.getAction() == EMode.SCALE) {
                if (!scalePoints.isEmpty()) {
                    scalePoints.removeLast();
                }

            } else if (action.getAction() == EMode.DRAG) {
                DragAction drag = ((DragAction) action);

                points.set(drag.getIndex(), drag.getFrom());
            }
        }


    }

    public boolean canUndo() {
        return !actions.isEmpty();
    }

    public boolean canRedo() {
        return !actionsToDo.isEmpty();
    }

    private boolean isSamePoint(Point point1, Point point2) {


        return (Math.abs(point1.x - point2.x) <= 5) && (Math.abs(point1.y - point2.y) <= 5);
    }

    public List<Point> bezier() {
        if (points.size() < 3) {
            return points;
        }

        List<Point> bezierPoints = new LinkedList<>();

        double cTension = 0.35;

        Point prev = points.get(0);
        bezierPoints.add(prev);

        Point[] d = points.toArray(new Point[points.size() + 2]);
        for (int i = d.length - 1; i > 0; i--) {
            d[i] = d[i - 1];
        }

        if (d.length >= 3) {
            d[d.length - 1] = d[d.length - 2].minus(d[d.length - 3].minus(d[d.length - 2]));
            d[0] = d[1].minus(d[2].minus(d[1]));
        }

        for (int i = 2; i < d.length - 1; i++) {
            Point ls = d[i - 1].plus(d[i]).mul(0.5);//center of the line segment

            Point tp = d[i - 1].plus(d[i - 1].minus(d[i - 2]).mul(cTension));//extending the prior line by 37.5%
            Point cp1 = ls.plus(tp).mul(0.5);//center of the extension to the center of the line segment

            tp = d[i].plus(d[i].minus(d[i + 1]).mul(cTension));
            Point cp2 = ls.plus(tp).mul(0.5);

            for (double j = 0; j < 1; j += 0.01) {
                Point next = d[i - 1].mul(Math.pow(1.0 - j, 3))
                        .plus(cp1.mul(3.0 * Math.pow(1.0 - j, 2) * j))
                        .plus(cp2.mul(3.0 * (1.0 - j) * (j * j)))
                        .plus(d[i].mul(j * j * j));

                bezierPoints.add(next);
            }
        }

        return bezierPoints;
    }

}
