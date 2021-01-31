package com.baizhi.utils;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GEOIpAndAgentUtils {

    private static LookupService lookupService = null;

    static {
        File file = new File("src/main/resources/GeoLiteCity-2013-01-18.dat");
        System.out.println(file.getAbsolutePath());
        try {
            lookupService = new LookupService(file, LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Object> parseLocaltion(String ip) {
        Location location = lookupService.getLocation(ip);
        String city = IPUtil.getCityInfo(ip);
        HashMap<String, Object> locationMap = new HashMap<>();
        locationMap.put("city", city);
        locationMap.put("point", new Float[]{location.longitude, location.latitude});
        return locationMap;
    }

    public static String parseAgentInfo(String agentInfo) {
        UserAgent agent = UserAgent.parseUserAgentString(agentInfo);
        Browser browser = agent.getBrowser();
        return browser.getName() + "|"
                + browser.getBrowserType().getName() + "|"
                + agent.getBrowserVersion().getVersion() + "|"
                + agent.getOperatingSystem().getManufacturer() + "|"
                + agent.getOperatingSystem().getName();

    }
}
