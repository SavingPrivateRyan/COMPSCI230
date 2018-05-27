/*Written by Ryan Martin-Gawn
 * rmar818
 * 584323162
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;
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
    private double barWidth;
    private int timeListBuilder;
    private int currentByteValue;
    private int ipLocation;
    private ArrayList<String> ipList;
    private int numberOfTicksY;
    private int tickDistanceY;
    private int yLabelValue;
    private int largestTransfer;
    private int bytesPerPixel;


    /** Constructor of the Plotting class for drawing the graph plot.
     * @param dataList ArrayList of String Array objects containing the data imported from a trace txt file.
     * @param sourceHosts ArrayList of Strings containing the Source IP addresses.
     * @param destinationHosts ArrayList of Strings containing the Destination IP addresses.
     * @param currentList boolean value of the current list selected in the JRadioButtons from the A2 Class.
     * @param currentIndex int value of the current index of the JComboBox from the A2 class.
     */
    public Plotting(ArrayList<String[]> dataList, ArrayList<String> sourceHosts, ArrayList<String> destinationHosts,
                    boolean currentList, int currentIndex) {
        this.dataList = dataList;
        this.sourceHosts = sourceHosts;
        this.destinationHosts = destinationHosts;
        this.currentList = currentList;
        this.currentIndex = currentIndex;
        setIpList();
        getTime();
        computeTime();
        findXAxis();
        computeBytes();
        yAxisScale();

    }

    /** Sets up what list is selected to be computed.
     */
    public void setIpList() {
        if (currentList) ipList = sourceHosts;
        else ipList = destinationHosts;
    }

    /** Method that changes the list that is being computed.
     * @param b
     */
    public void setList(boolean b) {
        if (b) {
            this.currentList = true;
            ipList = sourceHosts;
        }
        else {
            this.currentList = false;
            ipList = destinationHosts;
        }

    }

    /** Method that takes the updated index and then re-executes the calculations.
     * @param index
     */
    public void setIndex(int index) {
        this.currentIndex = index;
        computeBytes();
        yAxisScale();
    }

    /** Method that computes the amount of time in seconds the relevant trace file takes up for the graph.
     */
    public void getTime() {
        for (String[] traceLine : dataList) {
            if (traceLine[2].matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
                int dotPos = traceLine[1].indexOf(".");
                String s = traceLine[1].substring(0, dotPos);
                traceLine[1] = s;
                lastTime = traceLine[1];

            }
        }
    }

    /** Method that computes the time that will exist on the X-axis of the graph.
     */
    public void computeTime() {
        int preTime = Integer.parseInt(lastTime);
        if (preTime % 2 == 0) {
            preTime += 2;
        } else {
            preTime += 1;
        }
        totalTime = preTime;

    }

    /** Computes the tick distance on the X-Axis.
     */
    public void findXAxis() {
        numberOfTicks = totalTime / 50;
        tickDistance = 900 / numberOfTicks;
    }

    /** Creates an ArrayList the half the length of time with the total bytes sent or received in 2 second chunks.
     */
    public void computeBytes() {
        if (currentList) {
            ipLocation = 2;
        }
        else {
            ipLocation = 4;
        }
        timeListBuilder = 2;
        currentByteValue = 0;
        dataArray = new ArrayList<Integer>();
        for (String[] traceLine : dataList) {
            if (traceLine[ipLocation].matches(ipList.get(currentIndex))) {
                if (Integer.parseInt(traceLine[1]) < timeListBuilder) {
                    currentByteValue += Integer.parseInt(traceLine[7]);
                }
                else {
                    dataArray.add(currentByteValue);
                    currentByteValue = Integer.parseInt(traceLine[7]);
                    timeListBuilder += 2;
                }

            }
            else if (traceLine[ipLocation].matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
                if (Integer.parseInt(traceLine[1]) >= timeListBuilder) {
                        dataArray.add(currentByteValue);
                        currentByteValue = 0;
                        timeListBuilder += 2;
                }
            }
        }
        barWidth = 900.0 / dataArray.size();


    }

    /** Scales the Y-Axis based on the largest transfer window.
     */
    public void yAxisScale() {
        largestTransfer = 0;
        for (int transfer : dataArray) {
            if (transfer > largestTransfer) largestTransfer = transfer;
        }
        int checkValue = largestTransfer / 100000;
        numberOfTicksY = checkValue;
        yLabelValue = 100;
        int evenDivideChecker = 100000;
        int scaleIncrement = 1;
        bytesPerPixel = largestTransfer / 250;
        while (numberOfTicksY >= 10 && bytesPerPixel != 0) {
            yLabelValue *= 2;
            numberOfTicksY /= 2;
            scaleIncrement += 1;
        }
        while (numberOfTicksY < 4 && bytesPerPixel != 0) {
            yLabelValue /= 2;
            numberOfTicksY *= 2;
        }
        if (numberOfTicksY % 5 != 0){
            tickDistanceY = 250 / numberOfTicksY;

        }
        if (numberOfTicksY % 5 == 0) {
            if (bytesPerPixel == 0) tickDistanceY = 0;
            else {
                tickDistanceY = evenDivideChecker / (bytesPerPixel);
                tickDistanceY *= scaleIncrement;
            }
        }
    }


    /** Draw method that draws the graph of bytes sent of the duration onf the trace file.
     * @param g Graphics object passed in from the paintComponent.
     */
    public void draw(Graphics g) {
        int tickLocation = 50 + tickDistance;
        int tickNum = 50;
        int tickNumY = yLabelValue;
        int tickLocationY = 270 - tickDistanceY;
        for (int i = 0; i < numberOfTicks; i++) {
            g.drawLine(tickLocation, 270, tickLocation, 275);
            g.drawString(Integer.toString(tickNum), tickLocation - 10, 290);
            tickLocation += tickDistance;
            tickNum += 50;
        }
        for (int i1 = 0; i1 < numberOfTicksY; i1++) {
            g.drawLine(45, tickLocationY, 50, tickLocationY);
            String labelString;

            if (tickNumY >= 1000) {
                double printNum;
                labelString = "M";
                printNum = tickNumY / 1000.0;
                g.drawString(Double.toString(printNum) + labelString, 10, tickLocationY + 5);
            }
            else {
                int printNumInt;
                printNumInt = tickNumY;
                labelString = "k";
                g.drawString(Integer.toString(printNumInt) + labelString, 10, tickLocationY + 5);
            }
            tickLocationY -= tickDistanceY;
            tickNumY += yLabelValue;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.lightGray);
        double xValue = 50;
        double pixelsUp;
        double pixelValue = largestTransfer / 250.0;
        for (int box : dataArray) {
            if (bytesPerPixel != 0) pixelsUp = box / pixelValue;
            else pixelsUp = 0;
            g2d.draw(new Rectangle2D.Double(xValue, 270 - pixelsUp, barWidth, pixelsUp)); // Uses Rectangle2D.Double objects to draw the bars so that the graph can fit exactly to the graph Axis.
            xValue += barWidth;
        }


    }
}