/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakemeter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author yannick-broeker
 */
class ImageFilter extends FileFilter {

    public ImageFilter() {
    }

    @Override
    public boolean accept(File f) {
    if (f.isDirectory()) {
        return true;
    }

    String extension = Utils.getExtension(f);
    if (extension != null) {
        if (extension.equals(Utils.tiff) ||
            extension.equals(Utils.tif) ||
            extension.equals(Utils.gif) ||
            extension.equals(Utils.jpeg) ||
            extension.equals(Utils.jpg) ||
            extension.equals(Utils.png)) {
                return true;
        } else {
            return false;
        }
    }

    return false;
}

    @Override
    public String getDescription() {
       return "Only Images";
    }
    
}
