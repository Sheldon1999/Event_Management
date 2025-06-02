package com.eventmanagement.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading and caching images.
 * Provides methods to load, scale, and cache images efficiently.
 */
public class ImageLoader {
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    
    // Private constructor to prevent instantiation
    private ImageLoader() {}
    
    /**
     * Loads an image from the resources folder.
     * 
     * @param path The path to the image relative to the resources folder
     * @return The loaded ImageIcon, or null if not found
     */
    public static ImageIcon loadImage(String path) {
        // Check cache first
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        
        // Load from resources
        try {
            java.net.URL imageUrl = ImageLoader.class.getClassLoader().getResource(path);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                if (icon.getImage() != null) {
                    imageCache.put(path, icon);
                    return icon;
                }
            }
            System.err.println("Image not found: " + path);
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Loads and scales an image from the resources folder.
     * 
     * @param path The path to the image relative to the resources folder
     * @param width The desired width of the image (use -1 to maintain aspect ratio)
     * @param height The desired height of the image (use -1 to maintain aspect ratio)
     * @return The scaled ImageIcon, or null if not found
     */
    public static ImageIcon loadAndScaleImage(String path, int width, int height) {
        String cacheKey = path + "_" + width + "x" + height;
        
        // Check cache first
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        // Load the original image
        ImageIcon originalIcon = loadImage(path);
        if (originalIcon == null) {
            return null;
        }
        
        // Calculate dimensions while maintaining aspect ratio
        Image originalImage = originalIcon.getImage();
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        
        if (width <= 0 && height <= 0) {
            return originalIcon; // No scaling needed
        } else if (width <= 0) {
            // Scale based on height, maintain aspect ratio
            double ratio = (double) height / originalHeight;
            width = (int) (originalWidth * ratio);
        } else if (height <= 0) {
            // Scale based on width, maintain aspect ratio
            double ratio = (double) width / originalWidth;
            height = (int) (originalHeight * ratio);
        }
        
        // Scale the image with better quality
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        
        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        // Cache the scaled image
        imageCache.put(cacheKey, scaledIcon);
        
        return scaledIcon;
    }
    
    /**
     * Creates a circular version of an image.
     * 
     * @param path The path to the image relative to the resources folder
     * @param diameter The diameter of the circular image
     * @return The circular ImageIcon, or null if not found
     */
    public static ImageIcon createCircularImage(String path, int diameter) {
        String cacheKey = path + "_circle_" + diameter;
        
        // Check cache first
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        // Load and scale the image to fit the circle
        ImageIcon icon = loadAndScaleImage(path, diameter, diameter);
        if (icon == null) {
            return null;
        }
        
        // Create a circular image
        BufferedImage circleBuffer = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Create a circular clip
        java.awt.geom.Ellipse2D.Double circle = new java.awt.geom.Ellipse2D.Double(0, 0, diameter, diameter);
        g2.setClip(circle);
        
        // Draw the image centered in the circle
        Image image = icon.getImage();
        g2.drawImage(image, 0, 0, diameter, diameter, null);
        
        // Draw border
        g2.setClip(null);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(255, 255, 255, 150));
        g2.draw(circle);
        
        g2.dispose();
        
        ImageIcon circularIcon = new ImageIcon(circleBuffer);
        imageCache.put(cacheKey, circularIcon);
        
        return circularIcon;
    }
    
    /**
     * Clears the image cache to free up memory.
     */
    public static void clearCache() {
        imageCache.clear();
    }
}