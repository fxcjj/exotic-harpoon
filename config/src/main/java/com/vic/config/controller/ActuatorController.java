package com.vic.config.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 罗利华
 * date: 2020/12/16 18:06
 */
@RestController
@RequestMapping("actuator")
public class ActuatorController {

    @PostMapping("bus-refresh2")
    public Object busRefresh2(HttpServletRequest request, @RequestBody(required = false) String s) {
        System.out.println(s);
        return new ModelAndView("/actuator/bus-refresh");
    }

}
