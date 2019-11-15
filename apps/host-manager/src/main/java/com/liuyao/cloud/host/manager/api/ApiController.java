package com.liuyao.cloud.host.manager.api;

import com.liuyao.cloud.host.manager.api.beans.Beats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping("service")
@RestController
public class ApiController {

    @Autowired
    private DiscoveryClient client;

    @RequestMapping("getBeats")
    public Beats getService() {
        List<ServiceInstance> instances = client.getInstances("data-income");
        ServiceInstance info = instances.get(1);
        String appr = info.getHost() + info.getPort();
        RestTemplate restTemplate = new RestTemplate();
        return null;
    }
}
