package com.cb.itoken.web.admin.controller;

import com.cb.itoken.common.domain.TbSysUser;
import com.cb.itoken.common.dto.BaseResult;
import com.cb.itoken.common.utils.MapperUtils;
import com.cb.itoken.common.web.constants.WebConstants;
import com.cb.itoken.common.web.controller.BaseController;
import com.cb.itoken.web.admin.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController extends BaseController<TbSysUser, AdminService> {

    @Autowired
    private AdminService adminService;

    @ModelAttribute
    public TbSysUser tbSysUser(String userCode) {
        TbSysUser tbSysUser = null;

        if (StringUtils.isBlank(userCode)) {
            tbSysUser = new TbSysUser();
        } else {
            String json = adminService.get(userCode);
            try {
                tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 二次确认是否为空
        if(tbSysUser == null){
            tbSysUser = new TbSysUser();
        }

        return tbSysUser;
    }

    /**
     * 跳转首页
     *
     * @return
     */
    @RequestMapping(value = {"", "main"}, method = RequestMethod.GET)
    public String main() {
        return "main";
    }

    /**
     * 跳转首页
     *
     * @return
     */
    @RequestMapping(value = {"index"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /**
     * 跳转表单页
     *
     * @return
     */
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form() {
        return "form";
    }

    /**
     * 保存管理员
     *
     * @param tbSysUser
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(
            TbSysUser tbSysUser,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        // 初始化数据
        tbSysUser.setUserType("1");
        tbSysUser.setMgrType("1");
        tbSysUser.setStatus("0");
        tbSysUser.setCorpCode("0");
        tbSysUser.setCorpName("iToken");

        String tbSysUserJson = null;

        try {
            if(tbSysUser != null){
                tbSysUserJson = MapperUtils.obj2json(tbSysUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TbSysUser admin = (TbSysUser) request.getSession().getAttribute(WebConstants.SESSION_USER);
        String json = adminService.save(tbSysUserJson, admin.getUserCode());
        BaseResult baseResult = null;
        try {
            baseResult = MapperUtils.json2pojo(json, BaseResult.class);

            redirectAttributes.addFlashAttribute("baseResult", baseResult);
            if(baseResult.getSuccess().endsWith("成功")){
                return "redirect:/index";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/form";
    }
}
