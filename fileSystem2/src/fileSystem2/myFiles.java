package fileSystem2;

import java.io.File;

/**
 * Created by hongjiayong on 16/6/7.
 */
public class myFiles {
    private int blockName;
    private File myFile;
    private String fileName;
    double space;

    public myFiles(File myFile, int blockName, double capacity){
        space = capacity;
        this.myFile = myFile;
        this.blockName = blockName;
        fileName = myFile.getName();
    }

    public String getFileName(){
        return myFile.getName();
    }

    public String getFilePath(){
        return myFile.toString();
    }

    public boolean renameFile(String name){
        String c = myFile.getParent();
        File mm = new File(c + File.separator + name);
        if (myFile.renameTo(mm)){
            myFile = mm;
            fileName = name;
            return true;
        }else{
            return false;
        }
    }

    public File getMyFile(){
        return myFile;
    }

    public int getBlockName() {
        return blockName;
    }

    public double getSpace() {
        return space;
    }

    @Override
    public String toString(){
        return fileName;
    }
}
