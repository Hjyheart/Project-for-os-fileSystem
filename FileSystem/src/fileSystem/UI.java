package fileSystem;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by hongjiayong on 16/5/30.
 */
public class UI extends JFrame {
    private JTree tree;
    private JScrollPane treePane;
    private JScrollPane tablePane;
    private JTextField searchLine = new JTextField();
    private tableModel model = new tableModel();
    private JTable fileTable;
    private JPopupMenu myMenu = new JPopupMenu();
    private boolean searchLock = false;
    private int searchCount = 0;

    // delete a dir
    public void deleteDirectory(String filePath){
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

    // search file
    public String searchFile(String rootFile,String filePath){
        File root = new File(rootFile);
        File file = new File(filePath);
        String path = null;

        File [] files = root.listFiles();
        try {
            for (File myFile : files) {
                if (myFile.getName().equals(file.getName())) {
                    return myFile.getPath();
                }
                if (myFile.isDirectory()) {
                    path = searchFile(myFile.getPath(), filePath);
                }
            }
            searchCount++;
        }catch (Exception e){

        }
        return path;
    }

    public UI(){
        setLayout(new BorderLayout());

        // File tree init
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new myFiles(new File("")));

        final DefaultTreeModel treeModel = new DefaultTreeModel(root);

        final File [] roots = File.listRoots();
        for (File myFile : roots){
            DefaultMutableTreeNode node = null;
            myFiles addFile = new myFiles(myFile);
            node = new DefaultMutableTreeNode(addFile);
            if (myFile.isDirectory() && myFile.canRead() && myFile.listFiles().length != 0){
                node.add(new DefaultMutableTreeNode("temp"));
            }
            root.add(node);
        }

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
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                model.removeRows(0, model.getRowCount());
                if (parent!= null){
                    File rootFile = new File(((myFiles)parent.getUserObject()).getFilePath());
                    if (parent.getChildCount() > 0) {
                        File[] childFiles = rootFile.listFiles();

                        for (File myFile : childFiles) {
                            model.addRow(myFile);
                        }
                    }
                    else{
                        model.addRow(rootFile);
                    }
                    fileTable.updateUI();
                }
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
                if ((((DefaultMutableTreeNode)parent.getChildAt(0)).toString()).equals("temp") && parent.getChildCount() != 1)
                    treeModel.removeNodeFromParent(parent.getFirstLeaf());


                File rootFile = new File(((myFiles)parent.getUserObject()).getFilePath());
                File [] childFiles = rootFile.listFiles();

                model.removeRows(0, model.getRowCount());
                for (File myFile : childFiles){
                    DefaultMutableTreeNode node = null;
                    node = new DefaultMutableTreeNode(new myFiles(myFile));
                    if (myFile.isDirectory() && myFile.canRead() && myFile.listFiles().length != 0){
                        node.add(new DefaultMutableTreeNode("temp"));
                    }
                    treeModel.insertNodeInto(node, parent,parent.getChildCount());
                    model.addRow(myFile);
                }
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

        // Search init
        searchLine.setEditable(true);
        searchLine.setPreferredSize(new Dimension(1000, 50));
        JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pane.add(new Label("search"));
        pane.add(searchLine);
        JButton search = new JButton("ok");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!searchLock) {
                    searchLock = true;
                    String fileName = searchLine.getText();
                    String result = null;
//                    for (File myFile : roots) {
                        result = searchFile("/Users/hongjiayong", fileName);
//                        if (result != null)
//                            break;
//                    }
                    searchCount = 0;
                    System.out.println(result);
                    searchLock = false;
                }

            }
        });
        pane.add(search);
        add(pane, BorderLayout.NORTH);

        // Table init
        fileTable = new JTable(model);
        fileTable.setFont(new Font("黑体",Font.BOLD,20));
        fileTable.getTableHeader().setFont(new Font("黑体",Font.BOLD,32));
        fileTable.setRowHeight(30);

        tablePane = new JScrollPane(fileTable);
        add(tablePane, BorderLayout.CENTER);

        // Menu init

        // Create a file or a dir and update fileTable to show it
        JMenuItem createFileItem = new JMenuItem("创建");
        createFileItem.setFont(new Font("黑体",Font.BOLD,32));
        createFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                if (temp.getMyFile().isDirectory()) {
                    Object[] options = {"file", "directory"};
                    int type = JOptionPane.showOptionDialog(null, "Choose a type:", "create",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null,
                            options, options[0]);
                    System.out.println(type);
                    // create a file
                    if (type == 0){
                        String inputValue = JOptionPane.showInputDialog("File name:");
                        File newFile = new File(temp.getFilePath() + File.separator + inputValue);
                        if (!newFile.exists() && !inputValue.equals(null)){
                            try {
                                newFile.createNewFile();
                                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new myFiles(newFile));
                                treeModel.insertNodeInto(newNode, node, node.getChildCount());
                                tree.scrollPathToVisible(new TreePath(newNode.getPath()));
                                model.removeRows(0, model.getRowCount());
                                model.addRow(newFile);
                                fileTable.updateUI();
                                JOptionPane.showMessageDialog(null, "Create success!", "Success", JOptionPane.DEFAULT_OPTION);
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, "Create fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    // create a dir
                    if (type == 1){
                        String inputValue = JOptionPane.showInputDialog("Directory name:");
                        File newFile = new File(temp.getFilePath() + File.separator + inputValue);
                        if (!newFile.exists()){
                            newFile.mkdir();
                            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new myFiles(newFile));
                            newNode.add(new DefaultMutableTreeNode("temp"));
                            treeModel.insertNodeInto(newNode, node, node.getChildCount());
                            tree.scrollPathToVisible(new TreePath(newNode.getPath()));
                            model.removeRows(0, model.getRowCount());
                            model.addRow(newFile);
                            fileTable.updateUI();
                            JOptionPane.showMessageDialog(null, "Create success!", "Success", JOptionPane.DEFAULT_OPTION);
                        }else{
                            JOptionPane.showMessageDialog(null, "Create fail!!!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You must choose a directory!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete a file or a dir and update fileTable to show the difference
        JMenuItem deleteItem = new JMenuItem("删除");
        deleteItem.setFont(new Font("黑体",Font.BOLD,32));
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                // delete a file
                if (temp.getMyFile().isFile()){
                    temp.getMyFile().delete();
                    treeModel.removeNodeFromParent(node);
                    model.removeRow(temp.getFileName());
                    fileTable.updateUI();
                }
                // delete a dir
                if (temp.getMyFile().isDirectory()){
                    deleteDirectory(temp.getFilePath());
                    treeModel.removeNodeFromParent(node);
                    model.removeRow(temp.getFileName());
                    fileTable.updateUI();
                }
            }
        });

        // Rename a file and update fileTable to show the difference
        JMenuItem renameItem = new JMenuItem("重命名");
        renameItem.setFont(new Font("黑体",Font.BOLD,32));
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputValue = JOptionPane.showInputDialog("Old name:" + tree.getLastSelectedPathComponent().toString());
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                myFiles temp = (myFiles)node.getUserObject();
                if (temp.renameFile(inputValue)){
                    JOptionPane.showMessageDialog(null, "修改成功", "Yes!", JOptionPane.INFORMATION_MESSAGE);
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                    treeModel.removeNodeFromParent(node);
                    node = new DefaultMutableTreeNode(temp);
                    treeModel.insertNodeInto(node, parent, parent.getChildCount());
                    tree.scrollPathToVisible(new TreePath(node.getPath()));
                    model.removeRows(0, model.getRowCount());
                    model.addRow(temp.getMyFile());
                    fileTable.updateUI();
                }else{
                    JOptionPane.showMessageDialog(null, "修改失败", "No!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        myMenu.add(createFileItem);
        myMenu.add(deleteItem);
        myMenu.add(renameItem);
        myMenu.setPreferredSize(new Dimension(200, 200));

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
    public static void main(String args[]){
        new UI();
    }
}
