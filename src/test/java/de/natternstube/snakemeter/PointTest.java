package de.natternstube.snakemeter;

import org.assertj.core.api.Assertions;

/**
 * @author ybroeker
 */
public class PointTest {


    @org.junit.Test
    public void distance() throws Exception {
        //Arrange
        Point p1 = new Point(1,2);
        Point p2 = new Point(1,1);
        //Act

        double distance = p1.distance(p2);

        //Assert
        Assertions.assertThat(distance).isEqualTo(1);
    }

}
