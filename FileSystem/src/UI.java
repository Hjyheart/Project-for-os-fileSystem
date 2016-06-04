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
        searchLine.setPreferredSize(new Dimension(1200, 50));
        JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pane.add(new Label("search"));
        pane.add(searchLine);
        add(pane, BorderLayout.NORTH);

        // Table init
        fileTable = new JTable(model);
        fileTable.setFont(new Font("黑体",Font.BOLD,20));
        fileTable.getTableHeader().setFont(new Font("黑体",Font.BOLD,32));
        fileTable.setRowHeight(30);

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

        // Mouse DoubleClick
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
                    String fileName = ((String) model.getValueAt(fileTable.getSelectedRow(), 0));
                    String filePath = ((String) model.getValueAt(fileTable.getSelectedRow(), 1));
                    try {
                        Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler "  +  filePath);
                    } catch (IOException e1) {
                        System.out.println("fail");
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
