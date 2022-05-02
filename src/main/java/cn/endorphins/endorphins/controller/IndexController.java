package cn.endorphins.endorphins.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author {USER}
 * @createTime 2021年12月25日 22:37:00
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("")
    public String index(){
        return "success";
    }
}
