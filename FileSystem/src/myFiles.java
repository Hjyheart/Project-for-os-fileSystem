import java.io.File;

/**
 * Created by hongjiayong on 16/5/31.
 */
public class myFiles {
    private File myFile;

    public myFiles(File myFile){
        this.myFile = myFile;
    }

    public String getFileName(){
        return myFile.getName();
    }

    public String getFilePath(){
        return myFile.toString();
    }

    public void renameFile(String name){
        String c = myFile.getParent();
        File mm = new File(c + File.pathSeparator + name);
        if (myFile.renameTo(mm)){
            System.out.println("yes");
        }else{
            System.out.println("no");
        }
    }

    @Override
    public String toString(){
        return myFile.getName();
    }
}
