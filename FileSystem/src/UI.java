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
    private JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JButton addChild = new JButton("add");
    private JButton deleteChild = new JButton("delete");

    private JPopupMenu myMenu = new JPopupMenu();
    public UI(){
        setLayout(new BorderLayout());

        // File tree init
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new myFiles(new File("")));

        final DefaultTreeModel treeModel = new DefaultTreeModel(root);

        File [] roots = File.listRoots();
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
                if (parent.getChildCount() > 0){
                    File rootFile = new File(((myFiles)parent.getUserObject()).getFilePath());
                    File [] childFiles = rootFile.listFiles();

                    for (File myFile : childFiles){
                        model.addRow(myFile.getName(), myFile.getPath(), myFile.isFile(), myFile.length());
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
                    model.addRow(myFile.getName(), myFile.getPath(), myFile.isFile(), myFile.length());
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

//        // Add button init
//        addChild.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DefaultMutableTreeNode child = new DefaultMutableTreeNode("test");
//                DefaultMutableTreeNode parent = null;
//                TreePath parentPath = tree.getSelectionPath();
//                if (parentPath == null){
//                    parent = root;
//                }else{
//                    parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
//                }
//
//            }
//        });
//        buttons.add(addChild);
//
//        // Delete button init
//        deleteChild.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                treeModel.removeNodeFromParent((MutableTreeNode) tree.getLastSelectedPathComponent());
//            }
//        });
//        buttons.add(deleteChild);
//        add(buttons, BorderLayout.SOUTH);

        // Search init
        searchLine.setEditable(true);
        searchLine.setPreferredSize(new Dimension(1200, 50));
        JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pane.add(new Label("search"));
        pane.add(searchLine);
        add(pane, BorderLayout.NORTH);

        // Table init
        fileTable = new JTable(model);
        fileTable.setFont(new Font("黑体",Font.BOLD,32));
        fileTable.getTableHeader().setFont(new Font("黑体",Font.BOLD,32));
        fileTable.setPreferredSize(new Dimension(1500, 1000));
        fileTable.setRowHeight(50);
        tablePane = new JScrollPane(fileTable);
        add(tablePane, BorderLayout.CENTER);

        // Menu init
        JMenuItem createFileItem = new JMenuItem("创建");
        createFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JMenuItem deleteItem = new JMenuItem("删除");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JMenuItem renameItem = new JMenuItem("重命名");
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputValue = JOptionPane.showInputDialog("Old name:" + tree.getLastSelectedPathComponent().toString());
            }
        });
        myMenu.add(createFileItem);
        myMenu.add(deleteItem);
        myMenu.add(renameItem);

        // Mouse DoubleClick
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
                    String filePath = ((String) model.getValueAt(fileTable.getSelectedRow(), 1));
                    try {
                        Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler "  +  filePath);
                    } catch (IOException e1) {
                        System.out.println("fail");
                    }
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
