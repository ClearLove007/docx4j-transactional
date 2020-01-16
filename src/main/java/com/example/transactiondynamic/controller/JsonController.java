package com.example.transactiondynamic.controller;

import com.example.transactiondynamic.entity.MchtRankConfig;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 17:19 2020/1/7
 */
@RestController
@RequestMapping("/restful")
public class JsonController {

    @RequestMapping("/sub")
    public List<MchtRankConfig> subs(@RequestBody Map<String, Map<String, Map<String, String>>> request){
        request.forEach((k,v)->{
            String channelKind = k.substring(k.lastIndexOf("_")+1);
            v.forEach((k1,v1)->{
                String count = k1.substring(k1.lastIndexOf("_")+1);
                System.out.print(channelKind+"    "+count.charAt(0)+"    "+count.charAt(1)+"    ");
                v1.forEach((k2,v2)->{
                    System.out.print(k2+":"+v2+"  ");
                });
                System.out.println();
            });
        });
        return null;
    }
}
