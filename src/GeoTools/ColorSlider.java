package GeoTools;

import java.util.ArrayList;

import processing.core.PApplet;

public class ColorSlider {
	PApplet p;
	public ColorSlider(PApplet parent){p=parent;}
public ArrayList<Integer> colors = new ArrayList<Integer>();
	public int getGradient(float value) {
		value*=colors.size()-1;
		if (colors.size() == 0)
			return 0x000000;
		if (value <= 0.0)
			return (int) colors.get(0);
		if (value >= colors.size() - 1)
			return (int) colors.get(colors.size() - 1);
		int color_index = (int) value;
		int c1 = (int) colors.get(color_index);
		int c2 = (int) colors.get(color_index + 1);
		return p.lerpColor(c1, c2, value - color_index);
	}
}
