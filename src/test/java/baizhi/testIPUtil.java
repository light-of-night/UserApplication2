package baizhi;

import com.baizhi.UserApplication;
import com.baizhi.utils.IPUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class testIPUtil {
    @Test
    public void testIpUtil() {
        String ip = "218.70.26.255";
        String cityInfo = IPUtil.getCityInfo(ip);
        System.out.println(cityInfo);

    }


}
