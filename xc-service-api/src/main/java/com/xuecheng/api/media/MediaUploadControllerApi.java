/**
 * FileName: MediaUploadControllerApi
 * Author:   admin
 * Date:     2020/8/7 10:03
 * Description: 断点续传api
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.api.media;

import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈一句话功能简述〉<br> 
 * 〈断点续传api〉
 * @author admin
 * @create 2020/8/7
 * @since 1.0.0
 */
@Api(tags="断点续传api")
public interface MediaUploadControllerApi {

    @ApiOperation("文件上传注册")
    public ResponseResult register(String fileMd5,String fileName,Long fileSize,String mimeType,String fileExt);

    @ApiOperation("分片检查")
    public ResponseResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize);

    @ApiOperation("上传分块")
    public ResponseResult uploadChunk(MultipartFile file,Integer chunk,String fileMd5);

    @ApiOperation("合并分块")
    public ResponseResult mergeChunks(String fileMd5,String fileName,Long fileSize,String mimeType,String fileExt);
}
