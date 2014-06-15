
import processing.core.PApplet;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class ControlFrame extends PApplet {
  int w, h;
  public void setup() {
    size(w, h);
    frameRate(25);
    cp5 = new ControlP5(this);   
    cp5.addSlider("Range").plugTo(parent,"Range").setRange(0, 1).setPosition(20,10);
    cp5.addSlider("Dir").plugTo(parent,"Dir").setRange(0, 1).setPosition(20,40);
   // cp5.addSlider("Bay").plugTo(parent,"Bay").setRange(120, 200).setPosition(20,10);
   // cp5.addSlider("height").plugTo(parent,"height").setRange(100, 200).setPosition(20,50);
  //  cp5.addSlider("size").plugTo(parent,"size").setRange(1, 5).setPosition(20,90);
  }

  public void draw() {
      background(0);
  }
  
  @SuppressWarnings("unused")
private ControlFrame() { }
  public ControlFrame(Object theParent, int theWidth, int theHeight) {
    parent = theParent;
    w = theWidth;
    h = theHeight;
  }
  public ControlP5 control() {return cp5;}
  ControlP5 cp5;
  Object parent;  
}