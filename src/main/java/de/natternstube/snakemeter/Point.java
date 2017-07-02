package de.natternstube.snakemeter;

import java.util.logging.Logger;

/**
 * @author ybroeker
 */
public class Point {
    private static final Logger LOG = Logger.getLogger(Point.class.getName());
    final public double x, y;

    public Point(java.awt.Point point) {
        this(point.x, point.y);
    }

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public java.awt.Point toAWT() {
        return new java.awt.Point((int) x, (int) y);
    }

    public double distance(Point that) {
        return Math.sqrt(sq(that.x - this.x) + sq(that.y - this.y));
    }

    private double sq(double d) {
        return Math.pow(d, 2);
    }

    public Point minus(Point that) {
        return new Point(this.x - that.x, this.y - that.y);
    }

    public Point plus(Point that) {
        return new Point(this.x + that.x, this.y + that.y);
    }

    public Point mul(double scalar) {
        return new Point(this.x * scalar, this.y * scalar);
    }

}
