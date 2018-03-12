package net.tnemc.flatfile;

import net.tnemc.core.TNE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/18/2017.
 */
public class Utils {

  public static void zipDirectory(File directoryToCompress, File outputDirectory) {
    try {
      FileOutputStream destination = new FileOutputStream(new File(outputDirectory, directoryToCompress.getName() + ".zip"));
      ZipOutputStream zipOutputStream = new ZipOutputStream(destination);

      zipDirectoryHelper(directoryToCompress, directoryToCompress, zipOutputStream);
      zipOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void zipDirectoryHelper(File rootDirectory, File currentDirectory, ZipOutputStream out) throws Exception {
    byte[] data = new byte[2048];

    File[] files = currentDirectory.listFiles();
    if (files == null) {
      TNE.debug("");
    } else {
      for (File file : files) {
        if (file.isDirectory()) {
          zipDirectoryHelper(rootDirectory, file, out);
        } else {
          FileInputStream in = new FileInputStream(file);
          String name = file.getAbsolutePath().replace(rootDirectory.getAbsolutePath(), "");

          ZipEntry entry = new ZipEntry(name);
          out.putNextEntry(entry);
          int count;
          BufferedInputStream origin = new BufferedInputStream(in,2048);
          while ((count = origin.read(data, 0 , 2048)) != -1){
            out.write(data, 0, count);
          }
          origin.close();
        }
      }
    }

  }
}
