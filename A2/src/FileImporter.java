import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;


public class FileImporter {
    private File f;
    private Scanner input;

    public FileImporter(File f) {
        this.f = f;

    }

    public ArrayList<String[]> readData(){
        try {
            input = Scanner(Paths.get(f.getAbsolutePath()));

        }
    }
}
