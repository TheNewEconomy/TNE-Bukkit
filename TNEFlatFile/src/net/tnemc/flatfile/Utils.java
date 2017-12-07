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
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
