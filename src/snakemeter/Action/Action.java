package snakemeter.Action;

import java.awt.Point;

/**
 *
 * @author yannick-broeker
 */
public class Action {
    EAction action;
    Point point;

    public Action(EAction action, Point point) {
        this.action = action;
        this.point = point;
    }

    public EAction getAction() {
        return action;
    }

    public Point getPoint() {
        return point;
    }
    
    
}
