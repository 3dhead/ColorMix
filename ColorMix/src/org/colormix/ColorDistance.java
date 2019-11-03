package org.colormix;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class ColorDistance {
	private static int[] rgb2lab(int R, int G, int B) {
	    //http://www.brucelindbloom.com

	    float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
	    float Ls, as, bs;
	    float eps = 216.f / 24389.f;
	    float k = 24389.f / 27.f;

	    float Xr = 0.964221f;  // reference white D50
	    float Yr = 1.0f;
	    float Zr = 0.825211f;

	    // RGB to XYZ
	    r = R / 255.f; //R 0..1
	    g = G / 255.f; //G 0..1
	    b = B / 255.f; //B 0..1

	    // assuming sRGB (D65)
	    if (r <= 0.04045)
	        r = r / 12;
	    else
	        r = (float) Math.pow((r + 0.055) / 1.055, 2.4);

	    if (g <= 0.04045)
	        g = g / 12;
	    else
	        g = (float) Math.pow((g + 0.055) / 1.055, 2.4);

	    if (b <= 0.04045)
	        b = b / 12;
	    else
	        b = (float) Math.pow((b + 0.055) / 1.055, 2.4);


	    X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
	    Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
	    Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

	    // XYZ to Lab
	    xr = X / Xr;
	    yr = Y / Yr;
	    zr = Z / Zr;

	    if (xr > eps)
	        fx = (float) Math.pow(xr, 1 / 3.);
	    else
	        fx = (float) ((k * xr + 16.) / 116.);

	    if (yr > eps)
	        fy = (float) Math.pow(yr, 1 / 3.);
	    else
	        fy = (float) ((k * yr + 16.) / 116.);

	    if (zr > eps)
	        fz = (float) Math.pow(zr, 1 / 3.);
	    else
	        fz = (float) ((k * zr + 16.) / 116);

	    Ls = (116 * fy) - 16;
	    as = 500 * (fx - fy);
	    bs = 200 * (fy - fz);

	    int[] lab = new int[3];
	    lab[0] = (int) (2.55 * Ls + .5);
	    lab[1] = (int) (as + .5);
	    lab[2] = (int) (bs + .5);
	    return lab;
	}
	/**
	 * Computes the difference between two RGB colors by converting them to the L*a*b scale and
	 * comparing them using the CIE76 algorithm { http://en.wikipedia.org/wiki/Color_difference#CIE76}
	 */
	public static double colorDifference(Color a, Color b) {
	    int[] lab1 = rgb2lab(a.getRed(), a.getGreen(), a.getBlue());
	    int[] lab2 = rgb2lab(b.getRed(), b.getGreen(), b.getBlue());
	    return Math.sqrt(Math.pow(lab2[0] - lab1[0], 2) + Math.pow(lab2[1] - lab1[1], 2) + Math.pow(lab2[2] - lab1[2], 2));
	}
	
	public static double colorDifference2(Color e1, Color e2) {	
	    long rmean = ( (long)e1.getRed() + (long)e2.getRed() ) / 2;
	    long r = (long)e1.getRed() - (long)e2.getRed();
	    long g = (long)e1.getGreen() - (long)e2.getGreen();
	    long b = (long)e1.getBlue() - (long)e2.getBlue();
	    
	    
	    return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
	}
	
	public static Color mixColors(Color... colors) {
	    float ratio = 1f / ((float) colors.length);
	    int r = 0, g = 0, b = 0, a = 0;
	    for (Color color : colors) {
	        r += Math.pow(color.getRed(), 2.0) * ratio;
	        g += Math.pow(color.getGreen(), 2.0) * ratio;
	        b += Math.pow(color.getBlue(), 2.0) * ratio;
	        a += Math.pow(color.getAlpha(), 2.0) * ratio;
	    }
	    r = (int) Math.sqrt(r);
	    g = (int) Math.sqrt(g);
	    b = (int) Math.sqrt(b);
	    a = (int) Math.sqrt(a);
	    return new Color(r, g, b, a);
	}
	
	public static Color mixColors2(Color... colors) {
	    float ratio = 1f / ((float) colors.length);
	    int r = 0, g = 0, b = 0, a = 0;
	    for (Color color : colors) {
	        r += color.getRed() * ratio;
	        g += color.getGreen() * ratio;
	        b += color.getBlue() * ratio;
	        a += color.getAlpha() * ratio;
	    }
	    return new Color(r, g, b, a);
	}
	
	public static void findMix(Color targetColor) {
		Map<Double, String> distanceMap = new TreeMap<>();
		
		// one color
		for (WNCotmanPalette cotman : WNCotmanPalette.values()) {
			String newName = cotman.getName();
			Color newColor = cotman.getColor();
			newName += "#"+Integer.toHexString(newColor.getRGB()).substring(2);
			distanceMap.put(colorDifference(targetColor, cotman.getColor()), newName);
		}
		
		// two colors		
		for (WNCotmanPalette cotman1 : WNCotmanPalette.values()) {
			for (WNCotmanPalette cotman2 : WNCotmanPalette.values()) {
				if (cotman1 == cotman2) {
					continue;					
				}
				
				String newName = cotman1.getName() + " + " + cotman2.getName();
				Color newColor = mixColors(cotman1.getColor(), cotman2.getColor());
				newName += "#"+Integer.toHexString(newColor.getRGB()).substring(2);
				distanceMap.put(colorDifference(targetColor, newColor), newName);
			}			
		}
		
//		// three colors
//		for (WNCotmanPalette cotman1 : WNCotmanPalette.values()) {
//			for (WNCotmanPalette cotman2 : WNCotmanPalette.values()) {
//				for (WNCotmanPalette cotman3 : WNCotmanPalette.values()) {
//					if ((cotman1 == cotman2) && (cotman2 == cotman3)) {
//						continue;
//					}
//
//					String newName = cotman1.getName() + " + " + cotman2.getName() + " + " + cotman3.getName();
//					Color newColor = mixColors(cotman1.getColor(), cotman2.getColor(), cotman3.getColor());
//					newName += "#"+Integer.toHexString(newColor.getRGB()).substring(2);
//					distanceMap.put(colorDifference(targetColor, newColor), newName);
//				}
//			}
//		}
		
		String msg = "<html><body><div style='background-color: #"+Integer.toHexString(targetColor.getRGB()).substring(2)+";width:50px;height:50px'></div>";
		msg += "<div style='width:50px;height:25px'></div>";
		int count = 0;
		for (Entry<Double, String> entry : distanceMap.entrySet()) {
			String name = entry.getValue();
			String rgb = name.substring(name.lastIndexOf("#"));
			System.out.printf(Locale.US, "%-20s %8.2f%n", name, entry.getKey());
			msg += "<div style='background-color: "+rgb+";width:50px;height:50px'></div>"+name.substring(0, name.lastIndexOf("#"));;
			count++;
			if (count == 5) {
				break;
			}
		}
		msg += "</body></html>";
		
		System.out.println(msg);
		JOptionPane.showMessageDialog(null, msg);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {		
		// Bordeux new Color(122, 0, 92);
		
//		findMix(new Color(122, 0, 92));
//		findMix(Color.decode("#d0cfad"));
//		findMix(Color.decode("#ea7a58"));
		findMix(Color.decode("#c2a172"));
		
		

	}
}
