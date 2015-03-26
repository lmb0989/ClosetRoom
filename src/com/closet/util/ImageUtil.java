/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.closet.util;

/**
 *
 * @author Administrator
 */
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import javax.swing.*;  
import java.io.File;  
import java.io.IOException;  
import java.awt.*;  
import java.awt.image.BufferedImage;  
import java.awt.image.Kernel;  
import java.awt.image.ConvolveOp;  
import java.io.FileOutputStream;
  
public class ImageUtil {  
  
    public static BufferedImage getResizedImage(Image image, int newSize) {
  
        Image resizedImage = null;  
  
        int iWidth = image.getWidth(null);  
        int iHeight = image.getHeight(null);  
        if (iWidth > iHeight) {  
            resizedImage = image.getScaledInstance(newSize, (newSize * iHeight) / iWidth, Image.SCALE_SMOOTH);  
        } else {  
            resizedImage = image.getScaledInstance((newSize * iWidth) / iHeight, newSize, Image.SCALE_SMOOTH);  
        }  
  
        // This code ensures that all the pixels in the image are loaded.  
        Image temp = new ImageIcon(resizedImage).getImage();
  
        // Create the buffered image.  
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);  
  
        // Copy image to buffered image.  
        Graphics g = bufferedImage.createGraphics();  
  
        // Clear background and paint the image.  
        g.setColor(Color.white);  
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));  
        g.drawImage(temp, 0, 0, null);  
        g.dispose();  
  
        // Soften.  
        float softenFactor = 0.05f;  
        float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };  
        Kernel kernel = new Kernel(3, 3, softenArray);  
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
        bufferedImage = cOp.filter(bufferedImage, null);  
        
        return bufferedImage;
    }
    
    public static BufferedImage getResizedImage(File originalFile, int newSize){
        
        try {
            ImageIcon imageIcon = new ImageIcon(originalFile.getCanonicalPath());
            Image image = imageIcon.getImage();
            return getResizedImage(image, newSize);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static void getResizedImageFile(File originalFile, File resizedFile, int newSize, float quality) throws IOException{
        
        if (quality > 1) {  
            throw new IllegalArgumentException("Quality has to be between 0 and 1");  
        }  
                
        BufferedImage bufferedImage = getResizedImage(originalFile, newSize);
        FileOutputStream out = new FileOutputStream(resizedFile);  
  
        // Encodes image as a JPEG data stream  
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);  
        param.setQuality(quality, true);
  
        encoder.setJPEGEncodeParam(param);
        encoder.encode(bufferedImage);
    }
    
  
    public static void main(String[] args) throws IOException {  
//       File originalImage = new File("C:\\11.jpg");  
//       resize(originalImage, new File("c:\\11-0.jpg"),150, 0.7f);  
//       resize(originalImage, new File("c:\\11-1.jpg"),150, 1f);  
         File originalImage = new File("D:\\VirtualCloset\\upload\\test2.jpg");  
         getResizedImageFile(originalImage, new File("D:\\VirtualCloset\\upload\\1.jpg"),75, 0.7f);  
         getResizedImageFile(originalImage, new File("D:\\VirtualCloset\\upload\\2.jpg"),75, 1f);  
    }  
} 
