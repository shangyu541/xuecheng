/**
 * FileName: MediaController
 * Author:   admin
 * Date:     2020/8/7 10:19
 * Description: 断点续传
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈一句话功能简述〉<br> 
 * 〈断点续传〉
 * @author admin
 * @create 2020/8/7
 * @since 1.0.0
 */
@Api(tags = "断点续传",description = "断点续传测试")
@RestController
@RequestMapping("/media/upload")
public class MediaController implements MediaUploadControllerApi {

    /**
     * 1、上传前先把文件分成块
     * 2、一块一块的上传，上传中断后重新上传，已上传的分块则不用再上传
     * 3、各分块上传完成最后合并文件
     */

    /**
     * 服务端需要实现如下功能：
     * 1、上传前检查上传环境 检查文件是否上传，已上传则直接返回。 检查文件上传路径是否存在，不存在则创建。
     * 2、分块检查 检查分块文件是否上传，已上传则返回true。 未上传则检查上传路径是否存在，不存在则创建。
     * 3、分块上传 将分块文件上传到指定的路径。
     * 4、合并分块 将所有分块文件合并为一个文件。 在数据库记录文件信息。
     */

    @Autowired
    MediaService mediaService;

    @ApiOperation("文件上传注册")
    @Override
    @PostMapping("/register")
    public ResponseResult register(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName")String fileName,
                                   @RequestParam("fileSize")Long fileSize, @RequestParam("mimeType")String mimeType,
                                   @RequestParam("fileExt")String fileExt) {
        return mediaService.register(fileMd5,fileName,fileSize,mimeType,fileExt);
    }

    @ApiOperation("分片检查")
    @Override
    @PostMapping("/checkChunk")
    public CheckChunkResult checkChunk(@RequestParam("fileMd5")String fileMd5, @RequestParam("chunk")Integer chunk,
                                       @RequestParam("chunkSize")Integer chunkSize) {
        return mediaService.checkChunk(fileMd5,chunk,chunkSize);
    }

    @ApiOperation("上传分块")
    @Override
    @PostMapping("/uploadChunk")
    public ResponseResult uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("chunk")Integer chunk, @RequestParam("fileMd5")String fileMd5) {
        return mediaService.uploadChunk(file,chunk,fileMd5);
    }

    @ApiOperation("合并分片")
    @Override
    @PostMapping("/mergeChunks")
    public ResponseResult mergeChunks(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName")String fileName,
                                      @RequestParam("fileSize")Long fileSize, @RequestParam("mimeType")String mimeType,
                                      @RequestParam("fileExt")String fileExt) {
        return mediaService.mergeChunks(fileMd5,fileName,fileSize,mimeType,fileExt);
    }
}
