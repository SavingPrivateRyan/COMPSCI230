import javax.swing.*;
import java.awt.*;

public class DrawNetGraph extends JPanel {
    Font graphFont = new Font("Sans-serif", Font.PLAIN, 12);

    public DrawNetGraph() {
        super();
        this.setLayout(null);
        this.setLocation(0, 100);
        this.setSize(1000, 325);


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Volume [bytes]", 0, 10);
        g.drawString("Time [s]", 485, 320);
        g.drawLine(50, 20, 50, 275);
        g.drawLine(45, 270, 950, 270);
        g.drawString("0", 30, 275);
        g.drawString("0", 47, 290);
        int tickLocation = 125;
        int tickNum = 50;
        for (int i = 0; i < 12; i++){
            g.drawLine(tickLocation, 270, tickLocation, 275);
            g.drawString(Integer.toString(tickNum), tickLocation - 10, 290);
            tickLocation += 75;
            tickNum += 50;
        }



    }
}
