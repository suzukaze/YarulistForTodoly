package com.github.suzukaze.yarulistfortodoly.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {

  private static final String TAG = FileManager.class.getSimpleName();

  private Context context;

  public FileManager(Context context) {
    this.context = context;
  }

  public void saveFile(String filename, String text) {
    FileOutputStream out = null;
    try {
      File file = new File(String.format("%s/%s", getFileDir(), filename));
      out = new FileOutputStream(file);
      out.write(text.getBytes());
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public String loadFile(String filename) {
    String text = "";

    FileInputStream in = null;
    BufferedReader reader = null;

    try {
      File file = new File(String.format("%s/%s", getFileDir(), filename));
      FileInputStream fis = new FileInputStream(file);
      reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
      String line;
      while ((line = reader.readLine()) != null) {
        text += line + "\n";
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (Exception e) {
      }
    }

    return text;
  }


  public boolean makeDirs(String dirName) {
    String filename = String.format("%s/%s", getFileDir(), dirName);
    File file = new File(filename);
    return file.mkdirs();
  }

  public String getFileDir() {
    return context.getFilesDir().getAbsolutePath() + "/";
  }

  public boolean exists(String filename) {
    String path = getFileDir() + "/" + filename;
    File file = new File(path);
    return file.exists();
  }
}
