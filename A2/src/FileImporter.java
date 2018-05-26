import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;


public class FileImporter {
    private File f;
    private Scanner input;
    private String filePath;
    private ArrayList<String[]> dataList;

    /**
     * @param f
     */
    public FileImporter(File f) {
        this.f = f;
        filePath = f.getAbsolutePath();
        dataList = new ArrayList<String[]>();

    }

    /**
     * @return
     */
    public ArrayList<String[]> readData(){
        try {
            input = new Scanner(Paths.get(filePath));
        }
        catch (IOException ioExc) {


        }
        while (input.hasNext()) {
            String currentLine = input.nextLine();
            String[] strArray = currentLine.split("\\t");
            dataList.add(strArray);
        }

        return dataList;
    }
}