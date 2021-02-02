package com.shuen.splan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class PropertyManagerClient {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final Properties serverProperties = new Properties();
  
  private File serverPropertiesFile;
  
  public String comment;
  
  public PropertyManagerClient(File propertiesFile) {
    this.serverPropertiesFile = propertiesFile;
    this.comment = "Minecraft server properties";
    if (propertiesFile.exists()) {
      FileInputStream fileinputstream = null;
      try {
        fileinputstream = new FileInputStream(propertiesFile);
        this.serverProperties.load(fileinputstream);
      } catch (Exception exception) {
        LOGGER.warn("Failed to load {}", new Object[] { propertiesFile, exception });
        generateNewProperties();
      } finally {
        if (fileinputstream != null)
          try {
            fileinputstream.close();
          } catch (IOException iOException) {} 
      } 
    } else {
      LOGGER.warn("{} does not exist", new Object[] { propertiesFile });
      generateNewProperties();
    } 
  }
  
  public void generateNewProperties() {
    LOGGER.info("Generating new properties file");
    this.serverPropertiesFile.delete();
    try {
      this.serverPropertiesFile.createNewFile();
      saveProperties();
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.warn("Failed create new properties file " + this.serverPropertiesFile.getAbsolutePath());
    } 
  }
  
  public void saveProperties() {
    FileOutputStream fileoutputstream = null;
    try {
      fileoutputstream = new FileOutputStream(this.serverPropertiesFile);
      this.serverProperties.store(fileoutputstream, this.comment);
    } catch (Exception exception) {
      LOGGER.warn("Failed to save {}", new Object[] { this.serverPropertiesFile, exception });
      generateNewProperties();
    } finally {
      if (fileoutputstream != null)
        try {
          fileoutputstream.close();
        } catch (IOException iOException) {} 
    } 
  }
  
  public File getPropertiesFile() {
    return this.serverPropertiesFile;
  }
  
  public void setPropertiesFile(File f) {
    this.serverPropertiesFile = f;
  }
  
  public String getStringProperty(String key, String defaultValue) {
    if (!this.serverProperties.containsKey(key)) {
      this.serverProperties.setProperty(key, defaultValue);
      saveProperties();
      saveProperties();
    } 
    return this.serverProperties.getProperty(key, defaultValue);
  }
  
  public int getIntProperty(String key, int defaultValue) {
    try {
      return Integer.parseInt(getStringProperty(key, "" + defaultValue));
    } catch (Exception var4) {
      this.serverProperties.setProperty(key, "" + defaultValue);
      saveProperties();
      return defaultValue;
    } 
  }
  
  public long getLongProperty(String key, long defaultValue) {
    try {
      return Long.parseLong(getStringProperty(key, "" + defaultValue));
    } catch (Exception var5) {
      this.serverProperties.setProperty(key, "" + defaultValue);
      saveProperties();
      return defaultValue;
    } 
  }
  
  public boolean getBooleanProperty(String key, boolean defaultValue) {
    try {
      return Boolean.parseBoolean(getStringProperty(key, "" + defaultValue));
    } catch (Exception var4) {
      this.serverProperties.setProperty(key, "" + defaultValue);
      saveProperties();
      return defaultValue;
    } 
  }
  
  public void setProperty(String key, Object value) {
    this.serverProperties.setProperty(key, "" + value);
  }
  
  public boolean hasProperty(String key) {
    return this.serverProperties.containsKey(key);
  }
  
  public void removeProperty(String key) {
    this.serverProperties.remove(key);
  }
}
