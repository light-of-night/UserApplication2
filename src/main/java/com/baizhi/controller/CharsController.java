package com.baizhi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/chars")
public class CharsController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/yearCount")
    public Map<String, Object> yearCount(@RequestParam(value = "year") String year) {
        System.out.println("-----------------------------------");

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Set<String> keys = hashOperations.keys("monthUserCount:" + year);

        List<String> months = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        List<Integer> values = new ArrayList<Integer>();

        for (String month : months) {
            String value = hashOperations.get("monthUserCount:" + year, month);
            if (value == null || value.isEmpty()) {
                values.add(0);
            } else {
                values.add(Integer.parseInt(value));
            }
        }
        HashMap<String, Object> results = new HashMap<>();

        Set<Map.Entry<String, Object>> keyss = results.entrySet();
        for (Map.Entry<String, Object> stringObjectEntry : keyss) {
            System.out.println(stringObjectEntry + " " + results.get(stringObjectEntry));
        }

        results.put("category", months);
        results.put("data", values);

        return results;

    }
}
