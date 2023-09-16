/**
 * FileName: XcUserRepository
 * Author:   admin
 * Date:     2020/8/12 15:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {
    //查询用户
    XcUser findXcUserByUsername(String username);
}
