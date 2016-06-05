package fileSystem;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 * Created by hongjiayong on 16/5/30.
 */
public class Test {
    private FileOperation testCase = new FileOperation();

    @BeforeClass
    public static void start(){
        System.out.println("Start testing!\n");
    }

    @AfterClass
    public static void createSuccess(){
        System.out.println("fileSystem.Test over!");
    }

    @Before
    public void setUp() throws Exception{
        System.out.println("A test");
    }

    @org.junit.Test
    public void testCreate() throws Exception {
        if(testCase.createFile("/Users/hongjiayong/IdeaProjects/FileSystem/test.txt")){
            System.out.println("pass");
        }else{
            throw new Exception();
        }
    }

    @org.junit.Test
    public void testDelete() throws Exception {
        if (testCase.deleteFile("/Users/hongjiayong/IdeaProjects/FileSystem/test.txt")){
            System.out.println("pass");
        }else{
            throw new Exception();
        }
    }

    @org.junit.Test
    public void testCreateDirectory() throws Exception {
        if (testCase.createDirectory("/Users/hongjiayong/IdeaProjects/FileSystem/test")){
            System.out.println("pass");
        }else{
            throw new Exception();
        }
    }

//    @org.junit.Test
//    public void testDeleteDirectory() throws Exception{
//        testCase.deleteDirectory("/Users/hongjiayong/IdeaProjects/FileSystem/test");
//        System.out.println("pass");
//    }

    @org.junit.Test
    public void testGetFiles() throws Exception{
        testCase.getFiles();
        System.out.println("pass");
    }

}
