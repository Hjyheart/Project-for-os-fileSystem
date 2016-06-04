import java.io.File;

/**
 * Created by hongjiayong on 16/5/31.
 */
public class myFiles {
    private File myFile;
    private String fileName;

    public myFiles(File myFile){
        this.myFile = myFile;
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

    @Override
    public String toString(){
        return fileName;
    }
}
