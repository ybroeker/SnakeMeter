package de.natternstube.snakemeter;

import de.natternstube.snakemeter.action.Action;
import de.natternstube.snakemeter.action.DragAction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;

/**
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
        actions.add(new Action(EMode.SCALE, point));
        scalePoints.add(point);
    }

    public void addPoint(Point point) {
        actionsToDo.clear();
        actions.add(new Action(EMode.POINT, point));
        points.add(point);
    }

    public void addDrag(Point start, Point point) {
        actionsToDo.clear();
        if (actions.getLast() instanceof DragAction && (isSamePoint(((DragAction) actions.getLast()).getFrom(), start))) {
            DragAction dragAction = (DragAction) actions.getLast();
            dragAction.getPoint().setLocation(point);
            dragAction.setTo(new Point(point));

            System.out.println("" + start + "->" + point);
        } else {

            for (Point oldPoint : points) {
                if (isSamePoint(oldPoint, point)) {
                    oldPoint.setLocation(point);
                    actions.add(new DragAction(start, oldPoint, new Point(point)));
                }
            }
        }


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

            if (action.getAction() == EMode.POINT) {

                points.add(action.getPoint());

            } else if (action.getAction() == EMode.SCALE) {
                scalePoints.add(action.getPoint());

            } else if (action.getAction() == EMode.DRAG) {
                ((DragAction) action).getPoint().setLocation(((DragAction) action).getTo());
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
                ((DragAction) action).getPoint().setLocation(((DragAction) action).getFrom());
            }
        }


    }

    private boolean isSamePoint(Point point1, Point point2) {


        return (Math.abs(point1.x - point2.x) <= 5) && (Math.abs(point1.y - point2.y) <= 5);
    }

}
