/* Written by Ryan Martin-Gawn
 * rmar818
 * 584323162
 */

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

    /** Constructor of the FileImporter class.
     * @param f File object.
     */
    public FileImporter(File f) {
        this.f = f;
        filePath = f.getAbsolutePath();
        dataList = new ArrayList<String[]>();

    }

    /** Method that reads the data from the imported file.
     * @return Returns an ArrayList of String Arrays that hold each line of the trace file.
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