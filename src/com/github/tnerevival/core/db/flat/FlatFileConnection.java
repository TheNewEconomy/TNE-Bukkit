package com.github.tnerevival.core.db.flat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public class FlatFileConnection {

  private File file;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;

  public FlatFileConnection(String fileName) {
    file = new File(fileName);
  }

  public void close() {
    try {
      if(ois != null) {
        ois.close();
        ois = null;
      }

      if(oos != null) {
        oos.flush();
        oos.close();
        oos = null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return the fileName
   */
  public File getFile() {
    return file;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFile(String fileName) {
    file = new File(fileName);
  }

  /**
   * @return the ois
   */
  public ObjectInputStream getOIS() {
    if(ois == null) {
      try {
        ois = new ObjectInputStream(new FileInputStream(file));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return ois;
  }

  /**
   * @param ois the ois to set
   */
  public void setOIS(ObjectInputStream ois) {
    this.ois = ois;
  }

  /**
   * @return the oos
   */
  public ObjectOutputStream getOOS() {
    if(oos == null) {
      try {
        oos = new ObjectOutputStream(new FileOutputStream(file));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return oos;
  }

  /**
   * @param oos the oos to set
   */
  public void setOOS(ObjectOutputStream oos) {
    this.oos = oos;
  }
}