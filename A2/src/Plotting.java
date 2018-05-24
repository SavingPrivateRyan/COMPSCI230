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
    private int timeListBuilder;
    private int currentByteValue;
    private int ipLocation;
    private ArrayList<String> ipList;
    private int numberOfTicksY;
    private int tickDistanceY;
    private int yLabelValue;


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

    public void setIpList() {
        if (currentList) ipList = sourceHosts;
        else ipList = destinationHosts;
    }
    public void setList(boolean b) {
        if (b) {
            this.currentList = true;
            ipList = sourceHosts;
        }
        else {
            this.currentList = false;
            ipList = destinationHosts;
        }
        computeBytes();
    }

    public void setIndex(int index) {
        this.currentIndex = index;
        computeBytes();
        yAxisScale();
    }

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

    public void computeTime() {
        int preTime = Integer.parseInt(lastTime);
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
        barWidth = tickDistance / 25;
    }

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

    }

    public void yAxisScale() {
        int largestTransfer = 0;
        for (int transfer : dataArray) {
            if (transfer > largestTransfer) largestTransfer = transfer;
        }
        int checkValue = largestTransfer / 100000;
        if (checkValue % 5 != 0){
            numberOfTicksY = checkValue;
            while (numberOfTicksY > 10) {
                yLabelValue *= 2;
                numberOfTicksY /= 2;
            }
            tickDistanceY = 250 / numberOfTicksY;

        }
        if ((checkValue == 4) || ((checkValue >= 6) && (checkValue < 10))) {
            numberOfTicksY = checkValue;
            tickDistanceY = 250 / numberOfTicksY;
            yLabelValue = 100;
        }

        if (checkValue == 5 || checkValue == 10) {
            int bytesPerPixel = largestTransfer / 250;
            numberOfTicksY = checkValue;
            tickDistanceY = 100000 / (bytesPerPixel);
            yLabelValue = 100;
        }

        if (checkValue > 10 && checkValue < 15 || checkValue > 15 && checkValue < 20) {
            numberOfTicksY = largestTransfer / 200000;
            tickDistanceY = 250 / numberOfTicksY;
            yLabelValue = 200;
        }

        if (checkValue == 15 || checkValue == 20){
            numberOfTicksY = largestTransfer / 200000;
            int bytesPerPixel = largestTransfer / 250;
            tickDistanceY = 100000 / (bytesPerPixel);
            yLabelValue = 200;
        }

        System.out.println(largestTransfer + " " + tickDistanceY);
    }


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
            g.drawString(Integer.toString(tickNumY) + "k", 10, tickLocationY + 5);
            tickLocationY -= tickDistanceY;
            tickNumY += yLabelValue;
        }

    }
}
