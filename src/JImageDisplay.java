import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;
public class JImageDisplay extends JComponent {
private BufferedImage img;

public BufferedImage getImg(){
    return img;
}

public JImageDisplay(int width, int height){
    img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    Dimension dimension = new Dimension(width,height);
    super.setPreferredSize(dimension);
}
protected void paintComponent(Graphics j){
    super.paintComponent(j);
    j.drawImage(img,0,0,img.getWidth(),img.getHeight(),null);
}
public void clearImage(){
    for (int i = 0; i<img.getWidth();i++){
        for (int j=0;j<img.getHeight();j++){
            img.setRGB(i,j,0);
        }
    }
}
public void drawPixel(int x, int y, int color){
    img.setRGB(x,y,color);
}
}

