/**
 * FileName: MediaService
 * Author:   admin
 * Date:     2020/8/7 10:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author admin
 * @create 2020/8/7
 * @since 1.0.0
 */
@Service
public class MediaService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaService.class);

    @Autowired
    MediaFileRepository mediaFileRepository;

    @Value("${upload-location}")
    String uploadPath;

    /**
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimeType
     * @param fileExt
     * @return
     */
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt) {
        //检查文件是否上传
        //1.得到文件的路径
        String path = getFilePath(fileMd5, fileExt);
        File file = new File(path);
        //查询数据库文件是否存在
        Optional<MediaFile> byId = mediaFileRepository.findById(fileMd5);
        //文件存在直接返回
        if (file.exists() && byId.isPresent()) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //创建文件
        boolean fileFolder = CreateFileFolder(fileMd5);
        if (!fileFolder) {
            //文件目录创建失败
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 创建文件目录
     *
     * @param file
     * @return
     */
    private boolean CreateFileFolder(String fileMd5) {
        //创建文件上传目录
        String folderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(folderPath);
        if (!fileFolder.exists()) {
            boolean mkdirs = fileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }

    //得到文件所在目录
    private String getFileFolderPath(String fileMd5) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + File.separator + fileMd5.substring(1, 2) + File.separator + fileMd5 + File.separator;
        return filePath;
    }

    /**
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     *
     * @param fileMd5 文件md5值
     * @param fileExt 文件名
     * @return
     */
    private String getFilePath(String fileMd5, String fileExt) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + File.separator + fileMd5.substring(1, 2) + File.separator + fileMd5 + File.separator + fileMd5 + "." + fileExt;
        return filePath;
    }

    /**
     * 检查块文件
     *
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //块文件名称以1，2，3..序号命名，没有扩展名
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    /**
     * 得到块文件目录
     *
     * @param fileMd5
     * @return
     */
    private String getChunkFileFolderPath(String fileMd5) {
        String chunkFileFolderPath = getFileFolderPath(fileMd5) + File.separator + "chunks" + File.separator;
        return chunkFileFolderPath;
    }

    /**
     * 分块上传
     *
     * @param file
     * @param chunk
     * @param fileMd5
     * @return
     */
    public ResponseResult uploadChunk(MultipartFile file, Integer chunk, String fileMd5) {
        if (file == null) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_ISNULL);
        }
        //创建块文件目录
        boolean fileFolder = createChunkFileFolder(fileMd5);

        //块文件
        File chunkFile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        //上传的块文件
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 创建块文件目录
     *
     * @param fileMd5
     * @return
     */
    private boolean createChunkFileFolder(String fileMd5) {
        //创建上传文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            return mkdirs;
        }
        return true;
    }

    /**
     * 合并分块
     *
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimeType
     * @param fileExt
     * @return
     */
    public ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt) {
        //获取块文件的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //合并文件路径
        File mergeFile = new File(getFilePath(fileMd5, fileExt));
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!newFile){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CREATEFAIL);
        }
        //获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles=getChunkFiles(file);
        //合并文件
        mergeFile=mergeFile(mergeFile,chunkFiles);
        if (mergeFile==null){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        //校验文件中的MD5值
        boolean checkResult=this.checkFileMd5(mergeFile,fileMd5);

        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5+File.separator+fileExt);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFilePath(getFileFolderPathRelativePath(fileMd5,fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimeType);
        mediaFile.setFileType(fileExt);
        mediaFile.setFileStatus("301002");
        MediaFile save = mediaFileRepository.save(mediaFile);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private boolean checkFileMd5(File mergeFile, String fileMd5) {
        if (mergeFile==null || StringUtils.isEmpty(fileMd5)){
            return false;
        }
        FileInputStream fileInputStream=null;
        try {
            fileInputStream = new FileInputStream(mergeFile);
            //获取md5值
            String digestAsHex = DigestUtils.md5DigestAsHex(fileInputStream);
            if (fileMd5.equalsIgnoreCase(digestAsHex)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFileFolderPathRelativePath(String fileMd5, String fileExt) {
        String s = fileMd5.substring(0, 1) + File.separator + fileMd5.substring(1, 2) + File.separator + fileMd5 + File.separator;
        return s;
    }

    /**
     * 合并文件
     * @param mergeFile
     * @param chunkFiles
     * @return
     */
    private File mergeFile(File mergeFile, List<File> chunkFiles) {
        try {
            RandomAccessFile rw = new RandomAccessFile(mergeFile, "rw");
            byte[] bytes = new byte[1024];
            for (File chunkFile:chunkFiles
                 ) {
                int len=-1;
                RandomAccessFile randomAccessFile = new RandomAccessFile(chunkFile, "rw");
                while ((len=rw.read(bytes))!=-1){
                    randomAccessFile.write(bytes);
                }
                randomAccessFile.close();
            }
            rw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mergeFile;
    }

    /**
     * 块文件排序
     * @param file
     * @return
     */
    private List<File> getChunkFiles(File file) {
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName())<Integer.parseInt(o2.getName())){
                    return -1;
                }
                return 1;
            }
        });
        return fileList;
    }
}
