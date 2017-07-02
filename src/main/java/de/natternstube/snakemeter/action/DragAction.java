package de.natternstube.snakemeter.action;

import de.natternstube.snakemeter.EMode;
import de.natternstube.snakemeter.Point;


/**
 * @author yannick-broeker
 */
public class DragAction extends Action {

    final int index;
    final Point from;

    public DragAction(final int index, final Point from, final Point to) {
        super(EMode.DRAG, to);
        this.index = index;
        this.from = from;
    }

    public Point getFrom() {
        return from;
    }

    public int getIndex() {
        return index;
    }
}
