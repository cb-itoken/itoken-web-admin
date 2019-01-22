package com.cb.itoken.web.admin.service;

import com.cb.itoken.web.admin.service.fallback.AdminServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "itoken-service-admin", fallback = AdminServiceFallback.class)
public interface AdminService {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "loginCode") String loginCode, @RequestParam(value = "password") String password);

}
