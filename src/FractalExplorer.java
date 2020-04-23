import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class provides a GUI for displaying fractals. It also houses
 * a main function that runs the application.
 */
public class FractalExplorer {
    /** Side-length of our square display area. **/
    private int dispSize;
    /** Image area for our fractal. **/
    private JImageDisplay img;
    /** Used to generate fractals of a specified kind. **/
    private FractalGenerator fGen;
    /** The current viewing area in our image. **/
    private Rectangle2D.Double range;

    /** Basic constructor. Initializes the display image, fractal generator,
     * and initial viewing area.
     */
    public FractalExplorer(int dispSize) {
        this.dispSize = dispSize;
        this.fGen = new Mandelbrot();
        this.range = new Rectangle2D.Double(0, 0, 0, 0);
        fGen.getInitialRange(this.range);

    }

    /**
     * Sets up and displays the GUI.
     */
    public void createAndShowGUI() {
        /** Create the GUI components. **/
        JFrame frame = new JFrame("Fractal Explorer");
        img = new JImageDisplay(dispSize, dispSize);
        JButton resetButton = new JButton("Reset");

        /** Add listeners to components. **/
        ActionHandler aHandler = new ActionHandler();
        MouseHandler mHandler = new MouseHandler();
        resetButton.addActionListener(aHandler);
        img.addMouseListener(mHandler);

        /** Put all of the components into the Frame. **/
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(img, java.awt.BorderLayout.CENTER);
        frame.add(resetButton, java.awt.BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComboBox comboBox = new JComboBox();

        FractalGenerator mandelbrot = new Mandelbrot();
        comboBox.addItem(mandelbrot);
        FractalGenerator tricorn = new tricorn();
        comboBox.addItem(tricorn);
        FractalGenerator BurningShip = new BurningShip();
        comboBox.addItem(BurningShip);
        ButtonHandler choose = new ButtonHandler();
        comboBox.addActionListener(choose);

        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        panel.add(label);
        panel.add(comboBox);

        JButton save = new JButton("save");
        JPanel bottompanel = new JPanel();
        bottompanel.add(save);
        bottompanel.add(resetButton);
        frame.add(bottompanel,BorderLayout.SOUTH);
        ButtonHandler saveH = new ButtonHandler();
        save.addActionListener(saveH);
        frame.add(panel, BorderLayout.NORTH);
        /** Display the image. **/
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /** Use the fractal generator to draw the fractal pixel by pixel. **/
    private void drawFractal() {
        for (int i = 0; i < dispSize; i++) {
            for (int j = 0; j < dispSize; j++) {
                double xCoord = FractalGenerator.getCoord(range.x,
                        range.x + range.width, dispSize, i);
                double yCoord = FractalGenerator.getCoord(range.y,
                        range.y + range.width, dispSize, j);
                double numIters = fGen.numIterations(xCoord, yCoord);

                if (numIters == -1) {
                    /** The pixel is not in the set. Color it black. **/
                    img.drawPixel(i, j, 0);
                }
                else {
                    /** The pixel is in the fractal set.
                     *  Color the pixel based on the number of iterations
                     *  it took to escape.
                     */
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    img.drawPixel(i, j, rgbColor);
                }
            }
        }
        /** Don't forget to refresh the fractal image! **/
        img.repaint();
    }

    /** Simple handler to reset the zoom level. **/
    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fGen.getInitialRange(range);
            drawFractal();
        }
    }
    public class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            if(e.getSource() instanceof JComboBox){
                JComboBox source = (JComboBox)e.getSource();
                fGen = (FractalGenerator)source.getSelectedItem();
                fGen.getInitialRange(range);
                drawFractal();
            }
            else if (command.equals("Reset")){
                fGen.getInitialRange(range);
                drawFractal();
            }
            else if (command.equals("save")){
                JFileChooser fileChooser = new JFileChooser();
              //  FileFilter fileFilter = new FileNameExtensionFilter("PNG","*.png");
                // fileChooser.setFileFilter(fileFilter);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG", "*.png"));
                fileChooser.setAcceptAllFileFilterUsed(false);

                int select =  fileChooser.showSaveDialog(img);
                if (select == JFileChooser.APPROVE_OPTION){
                    java.io.File file = fileChooser.getSelectedFile();
                    String filename = file.toString();
                    try{
                        BufferedImage bufferedImage = img.getImg();
                        ImageIO.write(bufferedImage,"png",file);
                    } catch (Exception exception){
                        JOptionPane.showMessageDialog(img,exception.getMessage(),"Not save",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }


    /** Simple handler to zoom in on the clicked pixel. **/
    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = FractalGenerator.getCoord(range.x,
                    range.x + range.width, dispSize, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y,
                    range.y + range.width, dispSize, e.getY());
            fGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    /** Run the application. **/
    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
}
