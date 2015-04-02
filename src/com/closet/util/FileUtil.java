/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.closet.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author ºÚ¿Í
 */
public class FileUtil {
    
    public static void copy(File source, File dest){
        FileInputStream fin = null;
        try {
            System.out.println("source>>>>"+source);
            fin = new FileInputStream(source);
            BufferedInputStream in = new BufferedInputStream(fin);
            FileOutputStream fout = new FileOutputStream(dest);
            BufferedOutputStream out = new BufferedOutputStream(fout);
            int a;
            while ((a = in.read()) != -1) {
                out.write(a);
            }   out.flush();
            out.flush();
            fout.flush();
            fout.close();
            fin.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
