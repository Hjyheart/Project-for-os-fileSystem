package fileSystem2;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongjiayong on 16/6/7.
 */
public class Block {
    private int blockName;
    private File blockFile;
    private File blockBitMap;
    private File recover;
    private FileWriter bitWriter;
    private FileWriter recoverWriter;
    private int fileNum;
    private int space;
    public int [][] bitmap = new int[32][32];
    private Map<String, int[][] > filesBit = new HashMap<String, int[][]>();
    private ArrayList<File> files = new ArrayList<File>();

    public Block(int name, File file) throws IOException {
        space = 0;
        fileNum = 0;
        blockName = name;
        blockFile = file;
        blockFile.mkdir();
        blockBitMap = new File(blockFile.getPath() + File.separator + blockName + "BitMap&&Fat.txt");
        blockBitMap.createNewFile();
        bitWriter = new FileWriter(blockBitMap);
        for (int i = 0; i < 32; i++){
            for (int k = 0; k < 32; k++){
                bitmap[i][k] = 0;
                bitWriter.write("0");
            }
            bitWriter.write("\n");
        }
        bitWriter.flush();

        recover = new File(blockFile.getPath() + File.separator + "recover.txt");
        recover.createNewFile();
        recoverWriter = new FileWriter(recover);
        recoverWriter.write(String.valueOf(space) + "\n");
        recoverWriter.write(String.valueOf(fileNum) + "\n");
        for (int i = 0; i < 32; i++){
            for (int k = 0; k < 32; k++){
                if (bitmap[i][k] == 0){
                    recoverWriter.write("0\n");
                }else{
                    recoverWriter.write("1\n");
                }
            }
        }
        recoverWriter.flush();
    }

    public File getBlockFile(){
        return blockFile;
    }

    public FileWriter getBitWriter() {
        return bitWriter;
    }

    public File getBlockBitMap() {
        return blockBitMap;
    }

    public void rewriteBitMap() throws IOException {
        bitWriter = new FileWriter(blockBitMap);
        bitWriter.write("");
        for (int i = 0; i < 32;i++){
            for (int k = 0; k < 32; k++){
                if (bitmap[i][k] == 0){
                    bitWriter.write("0");
                }else{
                    bitWriter.write("1");
                }
            }
            bitWriter.write("\n");
        }
        for (int i = 0; i < files.size(); i++){
            bitWriter.write(files.get(i).getName() + ":");
            for (int k = 0; k < 32; k++){
                for (int j = 0; j < 32; j++){
                    try {
                        if (filesBit.get(files.get(i).getName())[k][j] == 1) {
                            bitWriter.write(String.valueOf(k * 32 + j) + " ");
                        }
                    }catch (Exception e){
                        System.out.println("wrong");
                    }
                }
            }
            bitWriter.write("\n");
        }
        bitWriter.flush();
    }

    public void rewriteRecoverWriter() throws IOException{
        recoverWriter = new FileWriter(recover);
        recoverWriter.write("");

        recoverWriter.write(String.valueOf(space) + "\n");
        recoverWriter.write(String.valueOf(fileNum) + "\n");
        for (int i = 0; i < 32; i++){
            for (int k = 0; k < 32; k++){
                if (bitmap[i][k] == 0){
                    recoverWriter.write("0\n");
                }else{
                    recoverWriter.write("1\n");
                }
            }
        }
        for (int i = 0; i < files.size(); i++){
            recoverWriter.write(files.get(i).getName() + "\n");
            int [][] bitTemp = filesBit.get(files.get(i).getName());
            for (int k = 0; k < 32; k++){
                for (int j = 0; j < 32; j++){
                    if (bitTemp[k][j] == 0){
                        recoverWriter.write("0\n");
                    }else {
                        recoverWriter.write("1\n");
                    }
                }
            }
        }
        recoverWriter.flush();
    }

    public boolean createFile(File file, double capacity) throws IOException {
        files.add(file);
        file.createNewFile();
        FileWriter newFileWriter = new FileWriter(file);
        int cap[][] = new int[32][32];
        for (int i = 0; i < 32; i++){
            for (int k = 0; k < 32; k++)
                cap[i][k] = 0;
        }
        BufferedReader in = new BufferedReader(new FileReader(blockBitMap));
        int count = (int) capacity;
        for (int i = 0; i < 32; i++){
            String line  = in.readLine();
            for (int k = 0; k < 32; k++){
                if (count > 0) {
                    if (line.charAt(k) == '0') {
                        count--;
                        cap[i][k] = 1;
                        bitmap[i][k] = 1;
                    }
                }
            }
        }
        if (count > 0){
            JOptionPane.showMessageDialog(null, "Insufficient memory!!", "Fail", JOptionPane.ERROR_MESSAGE);
            file.delete();
            for (int i = 0; i < 32; i++){
                for (int k = 0; k < 32; k++){
                    if (cap[i][k] == 1){
                        bitmap[i][k] = 0;
                    }
                }
            }
            return false;
        }else{
            fileNum++;
            space += capacity;
            filesBit.put(file.getName(), cap);
            rewriteBitMap();
            rewriteRecoverWriter();
            // Put FCB
            newFileWriter.write("File\n");
            newFileWriter.write(String.valueOf(capacity) + "\n");
            newFileWriter.write("Name: " + file.getName() + "\n");
            newFileWriter.write("Path: " + file.getPath() + "\n");
            String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(file.lastModified()));
            newFileWriter.write("Date last updated: " + ctime + "\n");
            newFileWriter.close();

            return true;
        }
    }

    public boolean deleteFile(File file, double capacity){
        if (file.getName().equals("1") || file.getName().equals("2") || file.getName().equals("3") || file.getName().equals("4")
                || file.getName().equals("5") || file.getName().equals("6") || file.getName().equals("7") || file.getName().equals("8")
                || file.getName().equals("9") || file.getName().equals("10") || file.getName().equals("1BitMap&&Fat.txt")
                || file.getName().equals("2BitMap&&Fat.txt") || file.getName().equals("3BitMap&&Fat.txt")
                || file.getName().equals("4BitMap&&Fat.txt") || file.getName().equals("5BitMap&&Fat.txt")
                || file.getName().equals("5BitMap&&Fat.txt") || file.getName().equals("6BitMap&&Fat.txt")
                || file.getName().equals("7BitMap&&Fat.txt") || file.getName().equals("8BitMap&&Fat.txt")
                || file.getName().equals("9BitMap&&Fat.txt") || file.getName().equals("10BitMap&&Fat.txt")){
            JOptionPane.showMessageDialog(null, "The dir is protected!!", "Access fail", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try{
            if (file.isFile()){
                try {
                    file.delete();
                }catch (Exception e){
                    e.printStackTrace();
                }
                space -= capacity;
                fileNum--;
                int[][] fileStore = filesBit.get(file.getName());
                for (int i = 0; i < 32; i++){
                    for (int k = 0; k < 32; k++){
                        if (bitmap[i][k] == 1 && fileStore[i][k] == 1){
                            bitmap[i][k] = 0;
                        }
                    }
                }
                filesBit.remove(file.getName());
                for (int i = 0; i < files.size(); i++){
                    if (files.get(i).getName().equals(file.getName())){
                        files.remove(i);
                        break;
                    }
                }
            }else{
                File [] files = file.listFiles();
                for(File myFile : files){
                    deleteFile(myFile, capacity);
                }
                while(file.exists()) {
                    file.delete();
                }
            }
            return true;
        }catch (Exception e){
            System.out.println("fail");
            return false;
        }
    }

    public boolean renameFile(File file, String name, double capacity) throws IOException {
        String oldName = file.getName();
        int[][] tempBit = filesBit.get(oldName);
        String c = file.getParent();
        File mm = new File(c + File.separator + name + ".txt");
        if (file.renameTo(mm)){
            file = mm;
            filesBit.remove(oldName);
            filesBit.put(file.getName(), tempBit);
            // Put FCB
            FileWriter newFileWriter = new FileWriter(file);
            newFileWriter.write("File\n");
            newFileWriter.write(String.valueOf(capacity) + "\n");
            newFileWriter.write("Name: " + file.getName() + "\n");
            newFileWriter.write("Path: " + file.getPath() + "\n");
            String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(file.lastModified()));
            newFileWriter.write("Date last updated: " + ctime + "\n");
            newFileWriter.close();
            for (int i = 0; i < files.size(); i++){
                if (files.get(i).getName().equals(oldName)){
                    files.remove(i);
                    files.add(file);
                    break;
                }
            }
            rewriteBitMap();
            rewriteRecoverWriter();
            return true;
        }else{
            return false;
        }
    }

    public int getFileNum() {
        return fileNum;
    }

    public int getSpace() {
        return space;
    }
}
