package com.cb.itoken.web.admin.service.fallback;

import com.cb.itoken.common.domain.TbSysUser;
import com.cb.itoken.common.hystrix.Fallback;
import com.cb.itoken.common.utils.MapperUtils;
import com.cb.itoken.web.admin.service.AdminService;
import org.springframework.stereotype.Component;

@Component
public class AdminServiceFallback implements AdminService {
    @Override
    public String get(String userCode) {
        return null;
    }

    @Override
    public String save(String tbSysUserJson, String optsBy) {
        return Fallback.badGateway();
    }

    @Override
    public String page(int pageNum, int pageSize, String tbSysUserJson) {
        return Fallback.badGateway();
    }
}
