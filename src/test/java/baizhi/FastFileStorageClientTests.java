package baizhi;

import com.baizhi.UserApplication;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.domain.upload.ThumbImage;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class FastFileStorageClientTests {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Test
    public void testUploadImage() throws IOException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\1.jpg");
        String path = uploadFile(inputStream, "aaa.jpg");
        System.out.println(path);
    }

    @Test
    public void testDeleteImage() throws IOException {
        fastFileStorageClient.deleteFile("group3/M00/00/00/wKhvgl0r8wGAFe11AAA0JnG2cpM110_150x150.jpg");
    }

    //    文件上传的封装
    private String uploadFile(InputStream inputStream, String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(suffix);
        try {
            FastImageFile fif = new FastImageFile(inputStream,
                    inputStream.available(), suffix,
                    new HashSet<>(), new ThumbImage(150, 150));
            StorePath storePath = fastFileStorageClient.uploadImage(fif);
            return storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    通过IP 得到 ip所对应的 地理位置 经度和纬度
    @Test
    public void testIpAddress() throws IOException {
        String ip = "1.202.251.26";
        File file = new File("src/main/resources/GeoLiteCity-2013-01-18.dat");
        System.out.println(file.getAbsolutePath());
        LookupService lookupService = new LookupService(file, LookupService.GEOIP_MEMORY_CACHE);
        Location location = lookupService.getLocation(ip);
        String city = location.city;

        System.out.println(city + " 纬度:" + location.latitude + " 经度" + location.longitude);
    }

    @Test
    public void testAgent() {
        String agentStr = "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
        UserAgent agent = UserAgent.parseUserAgentString(agentStr);
        Version browserVersion = agent.getBrowserVersion();
        Browser browser = agent.getBrowser();
        OperatingSystem operatingSystem = agent.getOperatingSystem();
        System.out.println(browser.getName() + " " + browser.getRenderingEngine() + " " + browserVersion.getVersion());
        System.out.println(operatingSystem.getManufacturer() + " " + operatingSystem.getName());
    }


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedis() {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Set<String> keys = hashOperations.keys("monthUserCount:" + 2019);

        List<String> months = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        List<Integer> values = new ArrayList<Integer>();

        for (String month : months) {
            String value = hashOperations.get("monthUserCount:" + 2019, month);
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
    }
}
