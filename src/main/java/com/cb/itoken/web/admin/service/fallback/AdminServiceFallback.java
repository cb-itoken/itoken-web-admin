package com.cb.itoken.web.admin.service.fallback;

import com.cb.itoken.common.constants.HttpStatusConstants;
import com.cb.itoken.common.dto.BaseResult;
import com.cb.itoken.common.utils.MapperUtils;
import com.cb.itoken.web.admin.service.AdminService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

@Component
public class AdminServiceFallback implements AdminService {
    @Override
    public String login(String loginCode, String password) {
        BaseResult baseResult = BaseResult.notOk(Lists.newArrayList(
                new BaseResult.Error(
                String.valueOf(HttpStatusConstants.BAD_GATEWAY.getStatus()),
                HttpStatusConstants.BAD_GATEWAY.getContent())));
        try {
            return MapperUtils.obj2json(baseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
