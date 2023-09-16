/**
 * FileName: UserService
 * Author:   admin
 * Date:     2020/8/12 15:15
 * Description: 用户service
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.domain.ucenter.response.UcenterCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户service〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
@Service
public class UserService {

    @Autowired
    XcUserRepository xcUserRepository;

    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;

    @Autowired
    XcMenuMapper xcMenuMapper;


    public XcUserExt getUserExt(String username) {
        //根据用户名成查询用户信息
        XcUser xcUser=this.findXcUserByUserName(username);
        if (xcUser==null){
//            ExceptionCast.cast(UcenterCode.UCENTER_ACCOUNT_NOTEXISTS);
            return null;
        }
        XcCompanyUser byUserId = xcCompanyUserRepository.findByUserId(xcUser.getId());
        String companyId=null;
        if (byUserId!=null){
             companyId = byUserId.getCompanyId();
        }
        List<XcMenu> xcMenuList=xcMenuMapper.selectPermissionByUserId(xcUser.getId());
        XcUserExt xcUserExt = new XcUserExt();
        //把所有属性都拷贝过去
        BeanUtils.copyProperties(xcUser,xcUserExt);
        xcUserExt.setCompanyId(companyId);
        xcUserExt.setPermissions(xcMenuList);
        return xcUserExt;
    }

    /**
     *  根据用户名成查询用户信息
     * @return
     * @param username
     */
    private XcUser findXcUserByUserName(String username) {
        XcUser xcUserByUsername = xcUserRepository.findXcUserByUsername(username);
        return xcUserByUsername;
    }
}
