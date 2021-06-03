package com.glowsoft.spring_test.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class HomeController {
    private String cookie;
    public List<String> data = new ArrayList<>();

    @RequestMapping(value = "/main.do", method = RequestMethod.GET)
    public String index(Model model) {
        //model.addAttribute("data", "Hello, Spring from IntelliJ! :)");
        data.add("");
        //model.addAttribute("data",data);
        System.out.println("main in!");
        return "main";
    }

    @RequestMapping(value = "/test")
    public ModelAndView test(){
        ModelAndView mav = new ModelAndView("main");
        mav.addObject("data","taesta");
        return mav;
    }

    @RequestMapping(value = "/naver.do")
    public String naver(){
        return "redirect:http://naver.com";
    }

    @RequestMapping(value = "/login.do")
    public ModelAndView ModelAndView(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("main");
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

        String name = request.getParameter("username");
        String password = request.getParameter("password");
        //List<String> data = new ArrayList<>();
        //data.add(request.getParameter("data"));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("username",name);
        params.add("password",password);
        params.add("REQUEST_KIND","API");

        HttpEntity entity = new HttpEntity(params,null);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/user/login/process", HttpMethod.POST, entity, String.class);
            cookie = responseEntity.getHeaders().get("SET-COOKIE").get(0);
            data.add(responseEntity.getStatusCode() + "\n" + responseEntity.getBody()+"\n\n");
            mav.addObject("data", data);
            response.addHeader("SET-COOKIE", cookie);
            System.out.println(responseEntity.getStatusCode());
            System.out.println(responseEntity.getBody());
            return mav;
        } catch (Exception e){
            data.add("fail\n\n");
            mav.addObject("data",data);
            return mav;
        }
    }

    @RequestMapping("/anal.do")
    public ModelAndView anal(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("main");
        //String data = request.getParameter("data");

        try {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie",cookie);

        String project = request.getParameter("project");
        String filepath = request.getParameter("filepath");


        List<String> files = new ArrayList<>();

        findFile(filepath,files);

        String analFiles = "";

        for(int i=0;i<files.size();i++){
            if(i!=files.size()-1)
                analFiles += files.get(i)+",";
            else
                analFiles += files.get(i);
        }

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
//      analFiles
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("projectname","test");
        params.add("filelist",analFiles);
        HttpEntity entity = new HttpEntity(params,headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/analyzeCluster", HttpMethod.POST, entity, String.class);
            data.add(responseEntity.getStatusCode() + "\n" + responseEntity.getBody()+"\n\n");
            mav.addObject("data",data);
            System.out.println(responseEntity.getStatusCode());
            System.out.println(responseEntity.getBody());
            return mav;
        }catch (Exception e){
            data.add("anal fail\n\n");
            mav.addObject("data",data);
            return mav;
        }
    }

    public static void findFile(String path,List<String> filelist){
        File[] files = new File(path).listFiles();

        for(File f : files){
            if(f.isDirectory()) {
                findFile(f.getAbsolutePath(),filelist);
            } else {
                filelist.add(f.getAbsolutePath());
            }
        }
    }
}