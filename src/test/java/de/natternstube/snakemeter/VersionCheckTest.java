package de.natternstube.snakemeter;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.fail;

/**
 * @author ybroeker
 */
//@RunWith(Enclosed.class)
public class VersionCheckTest {

    @Test
    public void testCon() throws Exception {

        try {
            URL url = new URL("https://github.com/ybroeker/SnakeMeter/releases/latest");
            URLConnection con = url.openConnection();
            con.getInputStream();

            String v = (con.getURL().toString());

            String[] elems = v.split("/");

            char[] chars = elems[elems.length - 1].toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    return;
                }
            }

            fail();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}