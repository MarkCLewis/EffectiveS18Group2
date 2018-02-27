package cloud;

import virtualworld.terrain.Perlin;
import java.math.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DirectDrawDemo extends JPanel {

    private BufferedImage canvas;
    private Perlin func;
    private double width;
    private double height;
    
    public DirectDrawDemo(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        float r = (float)1;
        float g = (float)1;
        float b = (float)1;
        float al = (float)(1);
        func = Perlin.getInstance();
        Color a = new Color(r,g,b,al);
        fillCanvas(Color.BLUE);
        //drawRect(a, 0, 0, width/2, height/2);
        
    }

    public double OctavePerlin(double x, double y, int octaves, double persistence) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
        for(int i=0;i<octaves;i++) {
            total += func.noise2D(x * frequency, y * frequency) * amplitude;
            
            maxValue += amplitude;
            
            amplitude *= persistence;
            frequency *= 2;
        }
        
        return total/maxValue;
    }
    
    
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }


    public void fillCanvas(Color c) {
        int color = c.getRGB();
        float r = 169/255;
        float b = 1;
        double yOff = 0;
        double xInc = 3/width;
        double yInc = 3/height;
        for (int y = 0; y < canvas.getHeight(); y++) {
        	float g = 1;
        	double xOff = 0;
            for (int x = 0; x < canvas.getWidth(); x++) {
            	b = (((float)(OctavePerlin(xOff,yOff, 2, 1) % 1.0)));
            	//b = (float)func.noise2D(xOff, yOff);
            	//System.out.print(b+" ");
            	b = (b+1) /2f;
            	//Color nColor = new Color(169/255,1,b,1);
            	Color nColor = new Color(0, 191,255, (int)(b * 255));
            	//if (b > 0.7)
            		//nColor = new Color(Math.abs(b - 0.5f), Math.abs(b - 0.5f), Math.abs(b - 0.5f), 1);
            	
            	//if (b < -0.7)
            		//nColor = new Color(Math.abs(b + 0.5f), Math.abs(b + 0.5f), Math.abs(b + 0.5f), 1);
            	//else
            	//	nColor = new Color(0,0, 0, 1);
            	canvas.setRGB(x, y, nColor.getRGB());
            	xOff += xInc;
                //g = (float)(g + 0.01) % 1;
            }
            yOff += yInc;
            //System.out.println();
            //b = (float) (b + 0.01) % 1;
        }
        repaint();
    }



    public void drawRect(Color c, int x1, int y1, int width, int height) {
        int color = c.getRGB();
        // Implement rectangle drawing
        for (int x = x1; x < x1 + width; x++) {
            for (int y = y1; y < y1 + height; y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }




    public static void main(String[] args) {
        int width = 1000;
        int height = 1000;
        JFrame frame = new JFrame("Direct draw demo");

        DirectDrawDemo panel = new DirectDrawDemo(width, height);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}