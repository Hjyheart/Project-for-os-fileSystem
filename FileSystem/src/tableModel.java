import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Created by hongjiayong on 16/6/1.
 */
public class tableModel extends AbstractTableModel {
    private Vector content = null;
    private String[] title_name = { "File Name", "File Path", "File Type", "File Volume/KB"};

    public tableModel(){
        content = new Vector();
    }

    public void addRow(File myFile){
        Vector v = new Vector();
        DecimalFormat format=new DecimalFormat("#0.00");
        v.add(0, myFile.getName());
        v.add(1, myFile.getPath());
        if (myFile.isFile()){
            v.add(2, "File");
            v.add(3, format.format((1.0 * myFile.length())/1024));
        }else {
            v.add(2, "Directory");
            v.add(3, "-");
        }
        content.add(v);
    }

    public void removeRow(String name) {
        for (int i = 0; i < content.size(); i++){
            if (((Vector)content.get(i)).get(0).equals(name)){
                content.remove(i);
                break;
            }
        }
    }

    public void removeRows(int row, int count){
        for (int i = 0; i < count; i++){
            if (content.size() > row){
                content.remove(row);
            }
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int colIndex){
        ((Vector) content.get(rowIndex)).remove(colIndex);
        ((Vector) content.get(rowIndex)).add(colIndex, value);
        this.fireTableCellUpdated(rowIndex, colIndex);
    }

    public String getColumnName(int col) {
        return title_name[col];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return false;
    }

    @Override
    public int getRowCount() {
        return content.size();
    }

    @Override
    public int getColumnCount() {
        return title_name.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector) content.get(rowIndex)).get(columnIndex);
    }
}
