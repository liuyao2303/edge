package com.liuyao.cloud.host.manager.api;

import com.liuyao.cloud.host.manager.api.beans.Beats;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("beats")
public class ApiController {

    @RequestMapping("get")
    public Beats getBeats() {
        Beats beats = new Beats();

        beats.setName("li");
        beats.setAddr("shengz");
        beats.setAge(13);
        return beats;
    }
}
