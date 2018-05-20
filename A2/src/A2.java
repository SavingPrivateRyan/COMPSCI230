import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class A2 extends JFrame implements ActionListener {

    private final Font font;
    private JPanel radioButtonPanel;
    private Font graphFont = new Font("Sans-serif", Font.PLAIN, 12);
    private DrawNetGraph netGraph;
    private JLabel  volumeLabel;



    public A2() {
        super("Flow volume viewer");
        setLayout(null);
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        font = new Font("Sans-serif", Font.PLAIN, 20);
        setupMenu();
        setupRadioButtons();
        netGraph = new DrawNetGraph();
        netGraph.setBackground(Color.WHITE);
        add(netGraph);
        setVisible(true);

    }
    public void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.setFont(font);
        menuBar.add(fileMenu);
        JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
        fileMenuOpen.setFont(font);
        fileMenu.add(fileMenuOpen);
        fileMenuOpen.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JFileChooser fileChooser = new JFileChooser(".");
                        int retval = fileChooser.showOpenDialog(A2.this);
                        if (retval == JFileChooser.APPROVE_OPTION) {
                            File f = fileChooser.getSelectedFile();
                        }
                    }
                }
        );

        JMenuItem fileMenuQuit = new JMenuItem("Quit");
        fileMenuQuit.setFont(font);
        fileMenu.add(fileMenuQuit);
        fileMenuQuit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

    }

    public void setupRadioButtons() {
        radioButtonPanel = new JPanel();
        radioButtonPanel.setSize(200, 100);
        radioButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        ButtonGroup radioButtons = new ButtonGroup();
        JRadioButton radioButtonSource = new JRadioButton("Source hosts");
        radioButtonSource.setFont(font);
        radioButtonSource.setSelected(true);
        radioButtons.add(radioButtonSource);
        radioButtonPanel.add(radioButtonSource, c);
        JRadioButton radioButtonDestination = new JRadioButton("Destination hosts");
        radioButtonDestination.setFont(font);
        radioButtons.add(radioButtonDestination);
        radioButtonPanel.add(radioButtonDestination, c);
        add(radioButtonPanel);
    }


    public void actionPerformed(ActionEvent event) {
        System.out.println('6');
    }
}
