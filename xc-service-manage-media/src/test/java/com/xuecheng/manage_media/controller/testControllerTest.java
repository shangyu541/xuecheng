package com.xuecheng.manage_media.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testControllerTest {

    /**
     * 文件分块的流程如下：
     * 1、获取源文件长度
     * 2、根据设定的分块文件的大小计算出块数
     * 3、从源文件读数据依次向每一个块文件写数据。
     */
    /**
     * 将文件分片
     * @throws IOException
     */
    @Test
    public void TestChunkSize() throws IOException {
        File sourceFile = new File("D:\\develop\\video\\ceshi.mp4");
        String chunkPath="D:/develop/ffmpeg/chunk/";
        File chunkFloder = new File(chunkPath);
        if (!chunkFloder.exists()) {
            chunkFloder.mkdirs();
        };
        //分块大小
        long chunkSize=1024*1024*1;
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        if (chunkNum<=0) {
            chunkNum=1;
        };
        //缓冲区大小
        byte[] bytes = new byte[1024];
        //使用RandomAccessFile访问文件
//        r	以只读的方式打开文本，也就意味着不能用write来操作文件
//        rw	读操作和写操作都是允许的
//        rws	每当进行写操作，同步的刷新到磁盘，刷新内容和元数据
//        rwd	每当进行写操作，同步的刷新到磁盘，刷新内容
        RandomAccessFile randomAccessFile = new RandomAccessFile(sourceFile,"r");
        //分块
        for (int i = 0; i <chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath+i);
            boolean newFile = file.createNewFile();
            if (newFile){
                //向分块中写入数据
                RandomAccessFile rw = new RandomAccessFile(file, "rw");
                int len=-1;
                while ((len=randomAccessFile.read(bytes))!=-1){
                    rw.write(bytes,0,len);
                    if (file.length()>chunkSize){
                        break;
                    }
                }
                rw.close();
            }
        }
        randomAccessFile.close();
    }

    /**
     * 分块合并的流程如下：
     * 1、找到要合并的文件并按文件合并的先后进行排序。
     * 2、创建合并文件
     * 3、依次从合并的文件中读取数据向合并文件写入数
     */
    @Test
    public void mergeChunkFund() throws IOException {
        //块文件目录
        File chunkFolder = new File("D:/develop/ffmpeg/chunk/");
        //要合成的路径和文件名称
        File mergefile = new File("D:/develop/ffmpeg/ceshi.mp4");
        if (mergefile.exists()) {
            //如果文件存在，删掉
            mergefile.delete();
        }
        //创建新的合并文件
        mergefile.createNewFile();
        RandomAccessFile rw = new RandomAccessFile(mergefile, "rw");
        //指定光标位置，这里指定文件顶端
        rw.seek(0);
        //缓冲区
        byte[] bytes = new byte[1024];
        //分块列表  list()方法是返回某个目录下的所有文件和目录的文件名，返回的是String数组
        //listFiles()方法是返回某个目录下所有文件和目录的绝对路径，返回的是File数组
        File[] files = chunkFolder.listFiles();
        //转成集合，便于排序
        List<File> fileList = new ArrayList<File>(Arrays.asList(files));
        //从小到大拍寻
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName())<Integer.parseInt(o2.getName())) {
                    return -1;
                }
                return 1;
            }
        });
        //合并文件
        for (File chunFile:fileList
             ) {
            RandomAccessFile rw_read = new RandomAccessFile(chunFile, "rw");
            int len=-1;
            while ((len=rw_read.read(bytes))!=-1){
                //将分块中的数据写入合并文件
                rw.write(bytes,0,len);
            }
            rw_read.close();
        }
        rw.close();
    }


}