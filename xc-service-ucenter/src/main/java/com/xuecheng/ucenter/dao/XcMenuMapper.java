/**
 * FileName: XcMenuMapper.xml
 * Author:   admin
 * Date:     2020/8/14 15:41
 * Description: dao
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈dao〉
 * @author admin
 * @create 2020/8/14
 * @since 1.0.0
 */
@Mapper
public interface XcMenuMapper {

    List<XcMenu> selectPermissionByUserId(String id);
}
