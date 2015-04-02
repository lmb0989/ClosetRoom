package com.closet.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class ApplicationConfig {

    public static String CONFIG_FILE;
    protected static TreeMap<String, String> CONFIG;

    static{
        CONFIG_FILE = System.getProperty("user.dir")+"/ClosetRoom.ini";
        if (!new File(CONFIG_FILE).exists()) { 
            CONFIG_FILE = System.getProperty("user.home") + "/ClosetRoom" + "/ClosetRoom.ini";
        }
        CONFIG = getConfig();
    }

    protected static TreeMap<String, String> getConfig() {
        try {
            TreeMap<String, String> data = new TreeMap<String, String>();
            FileReader r = new FileReader(new File(CONFIG_FILE));
            LineNumberReader lines = new LineNumberReader(r);
            String line;
            while ((line = lines.readLine()) != null) {
                if (line.startsWith(";")) {
                    continue;
                }
                int i = line.indexOf("=");
                if (i >= 0) {
                    String n = line.substring(0, i);
                    String v = line.substring(i + 1, line.length());
                    data.put(n, v);
                    System.setProperty(n, v);
                }
            }
            r.close();
            return data;
        } catch (Exception ex) {
            return new TreeMap<String, String>();
        }
    }

    public static void saveConfig() {
        synchronized (ApplicationConfig.class) {
            FileWriter w = null;
            try {
                w = new FileWriter(new File(CONFIG_FILE));
                for (String key : CONFIG.keySet()) {
                    w.write(key + "=" + CONFIG.get(key) + "\r\n");
                }
            } catch (Exception ex) {
            }
            try {
                w.flush();
            } catch (Exception ex) {
            }
            try {
                w.close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getConfig(String name, String defaultValue) {
        String result = CONFIG.get(name);
        System.out.println("result= " + result);
        if (result == null) {
            CONFIG.put(name, defaultValue);
            return defaultValue;
        }
        return result;
    }

    public static String getConfig(String name) {
        return CONFIG.get(name);
    }

    public static void saveConfig(String name, String value) {
        if (value != null) {
            CONFIG.put(name, value);
        } else {
            CONFIG.remove(name);
        }
        saveConfig();
    }
    
    public static String[] getConfigValues(String prefix) { 
        ArrayList<String> results = new ArrayList<>();
        for(String k : CONFIG.keySet()) { 
            if (k.startsWith(prefix)) results.add(CONFIG.get(k));
        }
        String[] data = new String[results.size()];
        results.toArray(data);
        return data;
    }
}
