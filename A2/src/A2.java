import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class A2 extends JFrame implements ActionListener, ItemListener {

    private Font font;
    private JPanel radioButtonPanel;
    private Font graphFont = new Font("Sans-serif", Font.PLAIN, 12);
    private JRadioButton radioButtonSource;
    private JRadioButton radioButtonDestination;
    private DrawNetGraph netGraph;
    private ArrayList<String[]> dataList;
    private ArrayList<String> srcHosts;
    private ArrayList<String> destHosts;
    private JComboBox<String> hostComboBox;
    private Plotting p;


    /**
     *
     */
    public A2() {
        super("Flow volume viewer");
        setLayout(null);
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        font = new Font("Sans-serif", Font.PLAIN, 20);
        setupArrayLists();
        setupMenu();
        setupRadioButtons();
        setupComboBox();
        netGraph = new DrawNetGraph();
        add(netGraph);
        setVisible(true);

    }

    /**
     *
     */
    public void setupArrayLists() {
        dataList = new ArrayList<String[]>();
        srcHosts = new ArrayList<String>();
        destHosts = new ArrayList<String>();
    }

    /**
     *
     */
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
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                        fileChooser.setFileFilter(filter);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        int retval = fileChooser.showOpenDialog(A2.this);
                        if (retval == JFileChooser.APPROVE_OPTION) {
                            File f = fileChooser.getSelectedFile();
                            FileImporter getData = new FileImporter(f);
                            dataList = getData.readData();
                            fillHostArrays();
                            hostComboBox.setVisible(true);
                            updateComboBox();
                            p = new Plotting(dataList, srcHosts, destHosts, radioButtonSource.isSelected(), hostComboBox.getSelectedIndex());
                            netGraph.setplot(p);
                            netGraph.repaint();

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

    /**
     *
     */
    public void setupRadioButtons() {
        radioButtonPanel = new JPanel();
        radioButtonPanel.setSize(200, 100);
        radioButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        ButtonGroup radioButtons = new ButtonGroup();
        radioButtonSource = new JRadioButton("Source hosts");
        radioButtonSource.setFont(font);
        radioButtonSource.setSelected(true);
        radioButtons.add(radioButtonSource);
        radioButtonPanel.add(radioButtonSource, c);
        radioButtonDestination = new JRadioButton("Destination hosts");
        radioButtonDestination.setFont(font);
        radioButtons.add(radioButtonDestination);
        radioButtonPanel.add(radioButtonDestination, c);
        radioButtonSource.addActionListener(this);
        radioButtonDestination.addActionListener(this);
        add(radioButtonPanel);
    }

    /**
     *
     */
    public void setupComboBox() {
        hostComboBox = new JComboBox<String>();
        hostComboBox.setModel((MutableComboBoxModel) new DefaultComboBoxModel());
        hostComboBox.setLocation(300, 38);
        hostComboBox.setSize(300,25);
        hostComboBox.setMaximumRowCount(8);
        hostComboBox.setFont(font);
        hostComboBox.setVisible(false);
        add(hostComboBox);

    }

    /**
     *
     */
    public void fillHostArrays() {
        srcHosts.clear();
        destHosts.clear();
        Comparator<String> ipComparator = new Comparator<String>() {
            public int compare(String ip1, String ip2) {
                return toNumeric(ip1).compareTo(toNumeric(ip2));
            }
        };

        Set<String> rawSource = new HashSet<String>();
        Set<String> rawDestination = new HashSet<String>();
        SortedSet<String> source = new TreeSet<String>(ipComparator);
        SortedSet<String> destination = new TreeSet<String>(ipComparator);

        for (String[] traceLine : dataList) {
            if (traceLine[2].matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
                rawSource.add(traceLine[2]);
            }
            if (traceLine[4].matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
                rawDestination.add(traceLine[4]);
            }
        }
        source.addAll(rawSource);
        destination.addAll(rawDestination);
        srcHosts.addAll(source);
        destHosts.addAll(destination);
    }

    /**
     * @param ip
     * @return
     */
    public Long toNumeric(String ip) {
            Scanner sc = new Scanner(ip).useDelimiter("\\.");
            Long l = (sc.nextLong() << 24) + (sc.nextLong() << 16) + (sc.nextLong() << 8)
                    + (sc.nextLong());

            sc.close();
            return l;
    }

    /**
     *
     */
    public void updateComboBox() {
        hostComboBox.removeItemListener(this);
        hostComboBox.removeAllItems();
        if (radioButtonSource.isSelected()) {
            for (String ip : srcHosts){
                hostComboBox.addItem(ip);
            }

        }
        else {
            for (String ip1 : destHosts) {
                hostComboBox.addItem(ip1);
            }
        }
        hostComboBox.addItemListener(this);

    }


    /**
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        if (radioButtonSource.isSelected()) {
            updateComboBox();
            if (!dataList.isEmpty()) {
                p.setList(true);
                p.setIndex(0);
                netGraph.repaint();
            }
        }
        if (radioButtonDestination.isSelected()) {
            updateComboBox();
            if (!dataList.isEmpty()) {
                p.setList(false);
                p.setIndex(0);
                netGraph.repaint();
            }

        }
    }

    /**
     * @param e
     */
    public void itemStateChanged(ItemEvent e) {
        if (hostComboBox.getSelectedItem() != null && e.getStateChange() == ItemEvent.SELECTED) {
            p.setIndex(hostComboBox.getSelectedIndex());
            netGraph.repaint();
        }
        return;
    }

}