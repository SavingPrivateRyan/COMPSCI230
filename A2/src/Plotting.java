import java.awt.*;
import java.util.*;

public class Plotting {

    private ArrayList<String[]> dataList;
    private ArrayList<String> sourceHosts;
    private ArrayList<String> destinationHosts;
    private ArrayList<Integer> dataArray;
    private boolean currentList;
    private int currentIndex;
    private String lastTime;
    private int totalTime;
    private int numberOfTicks;
    private int tickDistance;
    private int barWidth;


    public Plotting(ArrayList<String[]> dataList, ArrayList<String> sourceHosts, ArrayList<String> destinationHosts,
                    boolean currentList, int currentIndex) {
        this.dataList = dataList;
        this.sourceHosts = sourceHosts;
        this.destinationHosts = destinationHosts;
        this.currentList = currentList;
        this.currentIndex = currentIndex;
        getTime();
        computeTime();
        findXAxis();
        computeBytes();

    }

    public void setList(boolean b) {
        if (b) {
            currentList = true;
        }
        else {
            currentList = false;
        }
    }

    public void setIndex(int index) {
        this.currentIndex = index;
    }

    public void getTime() {
        for (String[] traceLine : dataList) {
            if (traceLine[2].matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
                lastTime = traceLine[1];

            }
        }
    }

    public void computeTime() {
        int dotPos = lastTime.indexOf(".");
        String s = lastTime.substring(0, dotPos);
        int preTime = Integer.parseInt(s);
        if (preTime % 2 == 0) {
            preTime += 2;
        } else {
            preTime += 1;
        }
        totalTime = preTime;
    }

    public void findXAxis() {
        numberOfTicks = totalTime / 50;
        tickDistance = 900 / numberOfTicks;
        barWidth = 900 / (totalTime / 2);
    }

    public void computeBytes(){
        for (String[] traceLine : dataList) {
            if (traceLine[2].matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {


            }
        }
    }


    public void draw(Graphics g) {
        int tickLocation = 50 + tickDistance;
        int tickNum = 50;
        for (int i = 0; i < numberOfTicks; i++) {
            g.drawLine(tickLocation, 270, tickLocation, 275);
            g.drawString(Integer.toString(tickNum), tickLocation - 10, 290);
            tickLocation += tickDistance;
            tickNum += 50;
            }
    }
}
