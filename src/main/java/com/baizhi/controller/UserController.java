package com.baizhi.controller;

import com.baizhi.entities.User;
import com.baizhi.exceptions.VerifyCodeException;
import com.baizhi.service.IUserService;
import com.baizhi.utils.GEOIpAndAgentUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.domain.upload.ThumbImage;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

@Controller// @Controller + @ResponseBody
@RequestMapping(value = "/formUserManager")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    //注入Service的实现类
    @Autowired
    private IUserService userService;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    //  一、用户的的登录
    @PostMapping(value = "/userLogin")
    @ResponseBody
    public void userLogin(User user, @RequestParam(value = "verifyCode") String verifyCode,
                          HttpSession session, HttpServletRequest request,
                          @CookieValue("inputVector") String inputVector,//用户的输入时间的采集
                          @RequestHeader("User-Agent") String agent) throws JsonProcessingException {

        //{"inputVector":"100,l10,110",location:{point:[x,y],city:"beijing"},agent:"设备信息..."}
//        日志数据的封装
        Map<String, Object> logs = new HashMap<>();
        logs.put("inputVector", inputVector);
        String[] ips = new String[]{"1.202.251.26", "116.234.222.36", "113.108.182.52", "1.198.72.174"};
        Map<String, Object> location = GEOIpAndAgentUtils.parseLocaltion(ips[new Random().nextInt(ips.length)]);
        logs.put("location", location);
        logs.put("agent", GEOIpAndAgentUtils.parseAgentInfo(agent));

        LOGGER.info("EVALUATE " + new ObjectMapper().writeValueAsString(logs));
        LOGGER.info("inputVector:" + inputVector + "| User-Agent:" + agent + " address:" + request.getRemoteAddr());
//      获取session中的验证码-- 及其验证
        String sessionVerifyCode = (String) session.getAttribute("verifyCode");
        if (sessionVerifyCode.equalsIgnoreCase(verifyCode)) {
            User loginUser = userService.login(user);
            session.setAttribute("user", loginUser);
            LOGGER.info("SUCCESS " + new ObjectMapper().writeValueAsString(logs));
        }
    }


    //  二、退出登录
    @RequestMapping(value = "/loginOut")
    public String loginOut(HttpSession session) {
        session.invalidate();
        return "redirect:/login.jsp";
    }


    // 三、 注册用户
    @PostMapping(value = "/registerUser")
    @ResponseBody
    public void registerUser(User user, @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "verifyCode") String verifyCode,
                             HttpSession session) throws Exception {

        String[] ips = new String[]{"1.202.251.26", "116.234.222.36", "113.108.182.52", "1.198.72.174"};
        Map<String, Object> location = GEOIpAndAgentUtils.parseLocaltion(ips[new Random().nextInt(ips.length)]);
//获取 系统产生的验证码。
        String sessionVerifyCode = (String) session.getAttribute("verifyCode");

        if (sessionVerifyCode.equalsIgnoreCase(verifyCode)) {
            session.removeAttribute("verifyCode");
            String path = "";
            if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
//                执行文件上传到FastDfs 文件系统
                path = uploadFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
            }
//            执行注册
            user.setPhoto(path);
            userService.registUser(user);
            session.setAttribute("user", user);

//            采集用户注册的地理位置
            LOGGER.info("REGISTER " + new ObjectMapper().writeValueAsString(location));
//            传统的文件的上传
//            String realPath = "E:/uploadfiles1";
//            File file = new File(realPath + "/" + multipartFile.getOriginalFilename());
//            multipartFile.transferTo(file);
//            user.setPhoto(multipartFile.getOriginalFilename());

        } else {
            throw new VerifyCodeException("验证码错误！");
        }

    }

    @PostMapping(value = "/addUser")
    @ResponseBody
    public void addUser(User user,
                        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {

        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            String path = uploadFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
            user.setPhoto(path);
        }
        userService.registUser(user);
    }

    //（1）、删
    @DeleteMapping(value = "/deleteByIds")
    @ResponseBody
    public void deleteByIds(@RequestParam(value = "ids") Integer[] ids) {
        userService.deleteByIds(ids);

    }

    //（2）、改
    @PutMapping(value = "/updateUser")
    @ResponseBody
    public void updateUser(User user) {
        userService.updateUser(user);

    }


    //（3）、查询
    @GetMapping("/queryById")
    @ResponseBody
    public User queryById(@RequestParam(value = "id") Integer id) {
        User user1 = userService.queryById(id);
        return user1;
    }

    // 分页查询
    @GetMapping("/queryUserByPage")
    @ResponseBody
    public Map<String, Object> queryUesrByPage(@RequestParam(value = "pageNow", defaultValue = "1") Integer pageNow,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "column", required = false) String column,
                                               @RequestParam(value = "value", required = false) String value) {
        HashMap<String, Object> results = new HashMap<>();
        results.put("total", userService.queryUserCount(column, value));
        results.put("rows", userService.queryByPage(pageNow, pageSize, column, value));

        return results;
    }


    //    文件上传到 Fastdfs
    private String uploadFile(InputStream inputStream, String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
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

}
