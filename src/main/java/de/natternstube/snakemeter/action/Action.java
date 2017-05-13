package de.natternstube.snakemeter.action;

import de.natternstube.snakemeter.EMode;

import java.awt.*;

/**
 *
 * @author yannick-broeker
 */
public class Action {
    EMode action;
    Point point;

    public Action(EMode action, Point point) {
        this.action = action;
        this.point = point;
    }

    public EMode getAction() {
        return action;
    }

    public Point getPoint() {
        return point;
    }
    
    
}
