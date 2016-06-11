package fileSystem2;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by hongjiayong on 16/6/7.
 */
public class UI extends JFrame {
    private JTree tree;
    private JScrollPane treePane;
    private JScrollPane tablePane;
    private tableModel model = new tableModel();
    private JTable fileTable;
    private JPopupMenu myMenu = new JPopupMenu();
    private JFileChooser chooser;

    private File rootFile;
    private File readMe;
    private FileWriter readMeWrite;

    private Block block1;
    private Block block2;
    private Block block3;
    private Block block4;
    private Block block5;
    private Block block6;
    private Block block7;
    private Block block8;
    private Block block9;
    private Block block10;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    private JLabel blockName = new JLabel("Block Name:");
    private JLabel nameField = new JLabel();
    private JLabel haveUsed = new JLabel("Used:");
    private JLabel usedField = new JLabel();
    private JLabel freeYet = new JLabel("Free:");
    private JLabel freeField = new JLabel();
    private JLabel fileNum = new JLabel("File Number:");
    private JLabel fileNumField = new JLabel();

    // Delete a dir
    public static void deleteDirectory(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            return;
        }
        if(file.isFile()){
            file.delete();
        }else if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File myfile : files) {
                deleteDirectory(filePath + "/" + myfile.getName());
            }
            file.delete();
        }
    }

    // Get space
    public double getSpace(File file){
        double space = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            space = Double.parseDouble(reader.readLine());
            if (space > 1024){
                space = 0.0;
            }
        } catch (FileNotFoundException E) {} catch (IOException E) {}
        return space;
    }

    public UI() throws IOException {
        setTitle("File System Demo");
        setLayout(new BorderLayout());


        // JFileChooser init
        String path = File.listRoots()[0].getPath();
        String rootPath = new String();
        chooser = new JFileChooser(path);
        chooser.setDialogTitle("Choose a dir for this demo");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == chooser.APPROVE_OPTION){
            System.out.println(chooser.getSelectedFile().getAbsolutePath());
            rootPath = chooser.getSelectedFile().getPath();
        }


        // Create work space
        rootFile = new File(rootPath + File.separator + "myFileSystem");
        readMe = new File(rootPath + File.separator + "myFileSystem" + File.separator + "ReadMe.txt");
        if (rootFile.exists()){
            deleteDirectory(rootFile.getPath());
        }
        rootFile.mkdir();
        readMe.createNewFile();
        FileWriter writer = new FileWriter(readMe.getPath());
        writer.write("Hello, this my file system!!!\n");
        writer.write("Space: 10 * 1024K = 10M(Block 0 is for FCB)\n");
        writer.write("Free-Space Management:bitmap\n");
        writer.write("Store-Space Management:FAT\n");
        writer.flush();

        block1 = new Block(1, new File(rootFile.getPath() + File.separator + "1"));
        blocks.add(block1);
        block2 = new Block(2, new File(rootFile.getPath() + File.separator + "2"));
        blocks.add(block2);
        block3 = new Block(3, new File(rootFile.getPath() + File.separator + "3"));
        blocks.add(block3);
        block4 = new Block(4, new File(rootFile.getPath() + File.separator + "4"));
        blocks.add(block4);
        block5 = new Block(5, new File(rootFile.getPath() + File.separator + "5"));
        blocks.add(block5);
        block6 = new Block(6, new File(rootFile.getPath() + File.separator + "6"));
        blocks.add(block6);
        block7 = new Block(7, new File(rootFile.getPath() + File.separator + "7"));
        blocks.add(block7);
        block8 = new Block(8, new File(rootFile.getPath() + File.separator + "8"));
        blocks.add(block8);
        block9 = new Block(9, new File(rootFile.getPath() + File.separator + "9"));
        blocks.add(block9);
        block10 = new Block(10, new File(rootFile.getPath() + File.separator + "10"));
        blocks.add(block10);

        // JTree init
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new myFiles(rootFile, 0, 10240));

        root.add(new DefaultMutableTreeNode(new myFiles(block1.getBlockFile(), 1, 1024.0)));
        model.addRow(new myFiles(block1.getBlockFile(), 1, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(0)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block2.getBlockFile(), 2, 1024.0)));
        model.addRow(new myFiles(block2.getBlockFile(), 2, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(1)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block3.getBlockFile(), 3, 1024.0)));
        model.addRow(new myFiles(block3.getBlockFile(), 3, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(2)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block4.getBlockFile(), 4, 1024.0)));
        model.addRow(new myFiles(block4.getBlockFile(), 4, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(3)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block5.getBlockFile(), 5, 1024.0)));
        model.addRow(new myFiles(block5.getBlockFile(), 5, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(4)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block6.getBlockFile(), 6, 1024.0)));
        model.addRow(new myFiles(block6.getBlockFile(), 6, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(5)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block7.getBlockFile(), 7, 1024.0)));
        model.addRow(new myFiles(block7.getBlockFile(), 7, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(6)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block8.getBlockFile(), 8, 1024.0)));
        model.addRow(new myFiles(block8.getBlockFile(), 8, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(7)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block9.getBlockFile(), 9, 1024.0)));
        model.addRow(new myFiles(block9.getBlockFile(), 9, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(8)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(block10.getBlockFile(), 10, 1024.0)));
        model.addRow(new myFiles(block10.getBlockFile(), 10, 1024.0));
        ((DefaultMutableTreeNode)root.getChildAt(9)).add(new DefaultMutableTreeNode("temp"));

        root.add(new DefaultMutableTreeNode(new myFiles(readMe, 0, 0)));
        model.addRow(new myFiles(readMe, 0, 0));

        // Table init
        fileTable = new JTable(model);
        fileTable.setFont(new Font("黑体",Font.BOLD,20));
        fileTable.getTableHeader().setFont(new Font("黑体",Font.BOLD,32));
        fileTable.setRowHeight(30);
        fileTable.setSelectionBackground(Color.ORANGE);

        fileTable.updateUI();

        final DefaultTreeModel treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setFont(new Font("黑体",Font.BOLD,32));
        tree.setRowHeight(50);
        tree.setEditable(false);
        tree.putClientProperty("Jtree.lineStyle",  "Horizontal");
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode parent = null;
                TreePath parentPath = e.getPath();
                if (parentPath == null){
                    parent = root;
                }else{
                    parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                }
                int blokName = ((myFiles)parent.getUserObject()).getBlockName();
                if (parentPath == null){
                    parent = root;
                }else{
                    parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                }

                myFiles myFile = (myFiles)parent.getUserObject();
                nameField.setText(myFile.getFileName());

                model.removeRows(0, model.getRowCount());
                File rootFile = new File(((myFiles)parent.getUserObject()).getFilePath());
                if (parent.getChildCount() > 0) {
                    File[] childFiles = rootFile.listFiles();

                    for (File file : childFiles) {
                        model.addRow(new myFiles(file, blokName, getSpace(file)));
                    }
                }
                else{
                    model.addRow(new myFiles(rootFile, blokName, getSpace(rootFile)));
                }
                fileTable.updateUI();

            }
        });
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                DefaultMutableTreeNode parent = null;
                TreePath parentPath = event.getPath();
                if (parentPath == null){
                    parent = root;
                }else{
                    parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                }

                int blokName = ((myFiles)parent.getUserObject()).getBlockName();

                File rootFile = new File(((myFiles)parent.getUserObject()).getFilePath());
                File [] childFiles = rootFile.listFiles();

                model.removeRows(0, model.getRowCount());
                for (File myFile : childFiles){
                    DefaultMutableTreeNode node = null;
                    node = new DefaultMutableTreeNode(new myFiles(myFile, blokName, 0));
                    if (myFile.isDirectory() && myFile.canRead()) {
                        node.add(new DefaultMutableTreeNode("temp"));
                    }

                    treeModel.insertNodeInto(node, parent,parent.getChildCount());
                    model.addRow(new myFiles(myFile, blokName, getSpace(myFile)));
                }
                if (parent.getChildAt(0).toString().equals("temp") && parent.getChildCount() != 1)
                    treeModel.removeNodeFromParent((MutableTreeNode) parent.getChildAt(0));
                fileTable.updateUI();
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                DefaultMutableTreeNode parent = null;
                TreePath parentPath = event.getPath();
                if (parentPath == null){
                    parent = root;
                }else{
                    parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                }
                if (parent.getChildCount() > 0) {
                    int count = parent.getChildCount();
                    for (int i = count - 1; i >= 0; i--){
                        treeModel.removeNodeFromParent((MutableTreeNode) parent.getChildAt(i));
                    }
                    treeModel.insertNodeInto(new DefaultMutableTreeNode("temp"), parent,parent.getChildCount());
                }
                model.removeRows(0, model.getRowCount());
                fileTable.updateUI();
            }
        });
        treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(350, 400));
        add(treePane, BorderLayout.WEST);

        tablePane = new JScrollPane(fileTable);
        add(tablePane, BorderLayout.CENTER);


        // Mouse DoubleClick to open a file
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
                    String fileName = ((String) model.getValueAt(fileTable.getSelectedRow(), 0));
                    String filePath = ((String) model.getValueAt(fileTable.getSelectedRow(), 1));
                    try {
                        if(Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(new File(filePath));
                        }
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry, some thing wrong!", "Fail to open",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(null, "File Name: " + fileName + "\n File Path: " + filePath, "content",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Menu init
        final JPopupMenu myMenu = new JPopupMenu();
        myMenu.setPreferredSize(new Dimension(300, 200));

        // Create a file and update fileTable to show it
        JMenuItem createFileItem = new JMenuItem("create a file");
        createFileItem.setFont(new Font("黑体",Font.BOLD,32));
        createFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                int blokName = temp.getBlockName();
                Block currentBlock = blocks.get(blokName - 1);

                String inputValue;
                int capacity;

                JOptionPane inputPane = new JOptionPane();
                inputPane.setInputValue(JOptionPane.showInputDialog("File name:"));
                if (inputPane.getInputValue() == null) {
                    return;
                }
                inputValue = inputPane.getInputValue().toString();
                inputPane.setInputValue(JOptionPane.showInputDialog("Large(KB):"));
                if (inputPane.getInputValue() == null) {
                    return;
                }
                capacity = Integer.parseInt(inputPane.getInputValue().toString());

                File newFile = new File(temp.getFilePath() + File.separator + inputValue + ".txt");
                if (!newFile.exists() && !inputValue.equals(null)){
                    try {
                        if (currentBlock.createFile(newFile, capacity)) {
                            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new myFiles(newFile, blokName, capacity));
                            model.removeRows(0, model.getRowCount());
                            model.addRow(new myFiles(newFile, blokName, capacity));
                            fileTable.updateUI();
                            JOptionPane.showMessageDialog(null, "Create success! Reopen the parent dir to reflash!", "Success", JOptionPane.DEFAULT_OPTION);
                        }
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Create fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        myMenu.add(createFileItem);

        // create a dir and update fileTable to show it
        JMenuItem createDirItem = new JMenuItem("create a dir");
        createDirItem.setFont(new Font("黑体",Font.BOLD,32));
        createDirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                int blokName = temp.getBlockName();
                String inputValue = JOptionPane.showInputDialog("Dir name:");
                if (inputValue == null) {
                    return;
                }
                File newDir = new File(temp.getFilePath() + File.separator + inputValue);
                if (newDir.exists())
                    deleteDirectory(newDir.getPath());
                try{
                    newDir.mkdir();
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new myFiles(newDir, blokName, 0));
                    newNode.add(new DefaultMutableTreeNode("temp"));
                    model.removeRows(0, model.getRowCount());
                    model.addRow(new myFiles(newDir, blokName, 0));
                    fileTable.updateUI();
                    JOptionPane.showMessageDialog(null, "Create success! Reopen the parent dir to reflash!", "Success", JOptionPane.DEFAULT_OPTION);
                }catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Create fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        myMenu.add(createDirItem);

        // Delete a file or a dir
        JMenuItem deleteItem = new JMenuItem("delete");
        deleteItem.setFont(new Font("黑体",Font.BOLD,32));
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                int blokName = temp.getBlockName();
                Block currentBlock = blocks.get(blokName - 1);
                int choose = JOptionPane.showConfirmDialog(null, "Are you sure to delete this file/dir?", "confirm", JOptionPane.YES_NO_OPTION);
                if (choose == 0){
                    if (currentBlock.deleteFile(temp.getMyFile(), temp.getSpace())){
                        try {
                            currentBlock.rewriteBitMap();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "Delete success! Reopen the parent dir to reflash!", "Success", JOptionPane.DEFAULT_OPTION);
                    }else{
                        JOptionPane.showMessageDialog(null, "Delete fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        myMenu.add(deleteItem);

        // Format a dir
        JMenuItem formatItem = new JMenuItem("format");
        formatItem.setFont(new Font("黑体",Font.BOLD,32));
        formatItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                int blokName = temp.getBlockName();
                Block currentBlock = blocks.get(blokName - 1);
                int choose = JOptionPane.showConfirmDialog(null, "Are you sure to format this dir?", "confirm", JOptionPane.YES_NO_OPTION);
                if (choose == 0){
                    try{
                    if (temp.getMyFile().isDirectory()) {
                        for (File myfile : temp.getMyFile().listFiles()) {
                            currentBlock.deleteFile(myfile, getSpace(myfile));
                        }
                        JOptionPane.showMessageDialog(null, "Format success! Reopen the parent dir to reflash!", "Success", JOptionPane.DEFAULT_OPTION);
                        currentBlock.rewriteBitMap();
                    }
                    }catch (Exception E1){
                        JOptionPane.showMessageDialog(null, "Format fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        myMenu.add(formatItem);

        // Rename a dir/file
        JMenuItem renameItem = new JMenuItem("rename");
        renameItem.setFont(new Font("黑体",Font.BOLD,32));
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                int blokName = temp.getBlockName();
                Block currentBlock = blocks.get(blokName - 1);

                String inputValue = null;
                JOptionPane inputPane = new JOptionPane();
                inputPane.setInputValue(JOptionPane.showInputDialog("New file name:"));
                if (inputPane.getInputValue() == null) {
                    return;
                }
                inputValue = inputPane.getInputValue().toString();
                try {
                    currentBlock.renameFile(temp.getMyFile(), inputValue, temp.getSpace());
                    JOptionPane.showMessageDialog(null, "Rename success! Reopen the parent dir to reflash!", "Success", JOptionPane.DEFAULT_OPTION);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Rename fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        myMenu.add(renameItem);

        // Information for the block
        JPanel panel = new JPanel();
        panel.setBackground(Color.green);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel tips = new JLabel("文件操作请选中左侧文件之后右键");
        tips.setFont(new Font("黑体",Font.BOLD,32));
        panel.add(tips);
        panel.add(blockName);
        panel.add(nameField);
        panel.add(haveUsed);
        panel.add(usedField);
        panel.add(freeYet);
        panel.add(freeField);
        panel.add(fileNum);
        panel.add(fileNumField);
        add(panel, BorderLayout.SOUTH);

        // Listen to the tree
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3){
                    myMenu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        });

        setSize(1600, 1200);
        setVisible(true);
    }

    public static void main(String args[]) throws IOException {
        new UI();
    }
}
