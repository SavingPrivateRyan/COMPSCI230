import javax.swing.SwingUtilities;

public class RunA2 implements Runnable {
    public void run() {
        A2 a = new A2();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunA2());
    }
}