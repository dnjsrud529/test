package com.glowsoft.spring_test.controller;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;


@Controller
public class HomeController {
    private String cookie;

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("data", "Hello, Spring from IntelliJ! :)");
        return "index";
    }

    @RequestMapping(value = "/test")
    public ModelAndView test(){
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("data","taesta");
        return mav;
    }

    @RequestMapping(value = "/naver.do")
    public String naver(){
        return "redirect:http://naver.com";
    }

    @RequestMapping(value = "code")
    public ModelAndView ModelAndView(HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("index");
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("username","admin");
//        map.put("password","1234qwer!");
//        map.put("REQUEST_KIND","API");
//        ObjectMapper obm = new ObjectMapper();
//        String params = "";
//        try {
//            params = obm.writeValueAsString(map);
//        }catch (Exception e){
//
//        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("username","admin");
        params.add("password","1234qwer!");
        params.add("REQUEST_KIND","API");

        HttpEntity entity = new HttpEntity(params,null);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/user/login/process", HttpMethod.POST,entity,String.class);
        cookie = responseEntity.getHeaders().get("SET-COOKIE").get(0);
        mav.addObject("data",cookie);
        response.addHeader("SET-COOKIE",cookie);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
        return mav;
    }

    @RequestMapping("anal")
    public ModelAndView anal(){
        ModelAndView mav = new ModelAndView("index");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie",cookie);

//        File file = new File("C:\\Users\\dnjsr\\pro_c\\pro_c.zip");
//
//        FileItem fileItem = null;
//
//        try {
//            fileItem = new DiskFileItem(null, Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
////            InputStream input = new FileInputStream(file);
////            OutputStream os = fileItem.getOutputStream();
////            IOUtils.copy(input, os);
//            // Or faster..
//            // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
//
//            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

//        } catch (IOException ex) {
//            // do something.
//        }
//
//        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

//        File file = new File("C:\\Users\\lje\\Desktop\\test.xlsx");
//        DiskFileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length() , file.getParentFile());
//
//        InputStream input = new FileInputStream(file);
//        OutputStream os = fileItem.getOutputStream();
//        IOUtils.copy(input, os);
//
//        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//
//
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("projectname","test");
        params.add("filelist","C:\\Users\\dnjsr\\pro_c\\cpdemo1\\cpdemo1.pc");
        HttpEntity entity = new HttpEntity(params,headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/analyzeCluster", HttpMethod.POST,entity,String.class);
        return mav;
    }
}