/*
 * This program takes a given .txt file in the format of a trace file and then displays the bytes sent or received
 * as a bar graph.
 *
 * Written by Ryan Martin-Gawn
 * rmar818
 * 584323162.
 */

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


    /** Constructor of the A2 Frame and everything inside.
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

    /** Sets up the ArrayLists that hold the trace file and the IP addresses.
     */
    public void setupArrayLists() {
        dataList = new ArrayList<String[]>();
        srcHosts = new ArrayList<String>();
        destHosts = new ArrayList<String>();
    }

    /** Adds the JMenu to the A2 Frame.
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

    /** Adds the JRadioButtons to the A2 Frame.
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

    /** Adds the initially invisible JComboBox to the A2 Frame.
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

    /** Takes the imported file and adds the IP addresses to the JComboBox.
     */
    public void fillHostArrays() {
        srcHosts.clear();
        destHosts.clear();
        Comparator<String> ipComparator = new Comparator<String>() {
            public int compare(String ip1, String ip2) {
                return toNumeric(ip1).compareTo(toNumeric(ip2));
            }
        }; //Anonymous Inner Class that sets up the comparator for the Treeset to sort the IP addresses.

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

    /** Takes a String IP address and returns a Long version of it that can be compared numerically.
     * @param ip String IP address.
     * @return l - the IP address as a Long value.
     */
    public Long toNumeric(String ip) {
        Scanner sc = new Scanner(ip).useDelimiter("\\.");
        Long l = (sc.nextLong() << 24) + (sc.nextLong() << 16) + (sc.nextLong() << 8)
                + (sc.nextLong());
        sc.close();
        return l;

    }

    /** Updates the IP Address values in the JComboBox.
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

    /** Action performed method that gets called when a radiobutton is activated.
     * @param event ActionEvent object that gets passed when the event occurs.
     */
    @Override
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

    /** Item state event that gets called when JComboBox changes it selected IP Address.
     * @param e ItemEvent object that gets passed when the JComboBox changed.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (hostComboBox.getSelectedItem() != null && e.getStateChange() == ItemEvent.SELECTED) {
            p.setIndex(hostComboBox.getSelectedIndex());
            netGraph.repaint();
        }
        return;
    }

}