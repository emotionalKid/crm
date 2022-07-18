package com.zhh.crm.workbench.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//workbenchController
@Controller
public class WorkbenchController {
    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
