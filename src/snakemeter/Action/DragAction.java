package snakemeter.Action;

import java.awt.Point;
import snakemeter.EMode;

/**
 *
 * @author yannick-broeker
 */
public class DragAction extends Action {

    Point from;
    Point to;

    public DragAction(Point from, Point point, Point to) {
        super(EMode.DRAG, point);
        this.from = from;
        this.to=to;
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

}
