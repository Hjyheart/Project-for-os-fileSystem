package fileSystem;

import java.io.File;
import java.io.IOException;

/**
 * Created by hongjiayong on 16/5/30.
 */
public class FileOperation {
    public FileOperation(){

    }
    // create a file
    public  boolean createFile(String filePath){
        boolean result = false;
        File file = new File(filePath);
        if(!file.exists()){
            try {
                result = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
    // delete a file
    public  boolean deleteFile(String filePath){
        boolean result = false;
        File file = new File(filePath);
        if(file.exists() && file.isFile()){
            result = file.delete();
        }

        return result;
    }
    // create a dir
    public  boolean createDirectory(String directory){
        boolean result = false;
        File file = new File(directory);
        if(!file.exists()){
            result = file.mkdirs();
        }

        return result;
    }
//    // delete a dir
//    public  static void deleteDirectory(String filePath){
//        File file = new File(filePath);
//        if(!file.exists()){
//            return;
//        }
//
//        if(file.isFile()){
//            file.delete();
//        }else if(file.isDirectory()){
//            File[] files = file.listFiles();
//            for (File myfile : files) {
//                deleteDirectory(filePath + "/" + myfile.getName());
//            }
//            file.delete();
//        }
//    }
    // get system files
    public void getFiles(){
        File [] roots = File.listRoots();
        for (File myFile : roots){
            System.out.println(myFile.toString());
            getChild(myFile);
        }
    }
    // get child
    public void getChild(File child){
        if (child.isFile()){
            System.out.println(child.toString());
            return;
        }
        if (child.isDirectory() && child.canRead()){
            File [] files = child.listFiles();
            for (File myFile : files){
                getChild(myFile);
            }
        }
    }


    public static void main(String args[]){

    }

}
