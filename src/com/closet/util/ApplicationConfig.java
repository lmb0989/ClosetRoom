package com.closet.util;

import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ApplicationConfig {

    public static String CONFIG_FILE;
    protected static TreeMap<String, String> CONFIG;

    public static final Random RAND = new Random(System.currentTimeMillis());
    
    public static interface EventHandler { public void fireEvent(String type, String msg); }
    
    public static ThreadLocal<EventHandler> GLOBAL_EVENT_SERVICE = new ThreadLocal<>();
    
    public static void fireEvent(String type, String msg) { 
        EventHandler e = GLOBAL_EVENT_SERVICE.get();
        if (e!=null) e.fireEvent(type,msg);
    }
    
    public static void setEventHandler(EventHandler e) { 
        GLOBAL_EVENT_SERVICE.set(e);
    }
    
    public static void init(String path) {
        CONFIG_FILE = path;
        CONFIG = getConfig();
    }

    private static String getContent(String resouce) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = ApplicationConfig.class.getResourceAsStream(resouce);
            int i;
            while ((i = in.read()) >= 0) {
                out.write(i);
            }
            out.flush();
            return new String(out.toByteArray(), "gb2312");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkReleaseNote(Component parent) {
        String releaseNote = getContent("/releasenote");
        if (releaseNote != null) {
            String currentHashCode = getConfig("releasenote.hashcode");
            String fileHashCode = releaseNote.hashCode() + "";
            if (!fileHashCode.equals(currentHashCode)) {
                showReleaseNote(parent);
                saveConfig("releasenote.hashcode", fileHashCode);
            }
        }
    }

    public static void showReleaseNote(Component parent) {
        String releaseNote = getContent("/releasenote");
        JTextArea textArea = new JTextArea(releaseNote);
        textArea.setColumns(50);
        textArea.setRows(40);
        textArea.setLineWrap(true);//超过设置的列数自动换行
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(parent, new JScrollPane(textArea), "版本信息", JOptionPane.PLAIN_MESSAGE);
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
