package com.example.kafka;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
public class TestController {

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @ApiOperation(value = "获取用户名")
    public ModelMap test(){
        ModelMap map = new ModelMap();
        map.addAttribute("username","jack");
        return map;
    }
}
