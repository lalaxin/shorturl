package shorturl.demo.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.DateUtils;
import shorturl.demo.entity.Surl;
import shorturl.demo.server.surlServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Controller
public class surlController {
    @Autowired
    surlServer surls;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

//    生成短网址
    @ResponseBody
    @RequestMapping("/create")
    public String createShortUrl(String longUrl, String viewPwd, HttpServletRequest request){
        JSONObject json=new JSONObject();
        String[] split=longUrl.split("\n|\r");
        StringBuffer msg=new StringBuffer();
        for(int i=0;i<split.length;i++){
//            先给输入的网址加上http（若它没有的话）
            if(!split[i].contains("http://") && !split[i].contains("https//")){
                split[i]="http://"+split[i];
            }

            Surl surl=new Surl();
//            随机生成一个UUId作为主键，防止重复
            surl.setUrlId(UUID.randomUUID().toString());
//            随机生成一个六位字符作为短网址
            String shortUrlId=getStringRandom(6);
            surl.setShortId(shortUrlId);
            surl.setLongUrl(split[i]);
            surl.setCreateTime(DateUtils.format(new Date(),"yyyy-MM-dd HH-mm-ss", Locale.SIMPLIFIED_CHINESE));
            surl.setViewPwd(viewPwd);

            Boolean flag=surls.createurl(surl);

            String toUrl="/";
//            request.getServerPort获取当前服务器端口
            int serverPort=request.getServerPort();
            if(serverPort==80 || serverPort==443){
//                request.getScheme()返回当前连接使用的协议=http  request.getServerName返回当前网站的域名(服务器名称)=localhost
                toUrl=request.getScheme()+"://"+request.getServerName();
            }else {
                toUrl=request.getScheme()+"://"+request.getServerName()+":"+serverPort;
            }
            if(flag){
                msg.append(toUrl+"/"+shortUrlId+"<br>");
            }

            json.put("shortUrl",msg);

        }
        return json.toJSONString();

    }

    public String getStringRandom(int length){
        String val="";
        Random random=new Random();

        for(int i=0;i<length;i++){
            String charOrNum=random.nextInt(2)%2==0?"char":"num";
            if("char".equalsIgnoreCase(charOrNum)){
                int temp=random.nextInt(2)%2==0?65:97;
                val+=(char)(random.nextInt(26)+temp);
            }else {
                int temp=random.nextInt(10);
                val+=String.valueOf(temp);
            }
        }

        return val;

    }

//    访问短链接
    @RequestMapping(value = "/{shortUrlId}")
    public void view(@PathVariable("shortUrlId") String shortUrlId, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        Surl surl=surls.findByShortId(shortUrlId);
        if(surl!=null){
            if(surl.getViewPwd()!=null && !surl.getViewPwd().equals("")){
                request.setAttribute("shortUrlId",shortUrlId);
//请求转发，跳转到其他页面
                request.getRequestDispatcher("/viewPwd").forward(request,response);
            }else {
                surls.updateShortUrl(shortUrlId);
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location",surl.getLongUrl());
            }
        }else {
            request.getRequestDispatcher("/noPage").forward(request,response);
        }
    }

    @RequestMapping("noPage")
    public String noPage(){
        return "noPage";
    }

    @RequestMapping("viewPwd")
    public String viewPwd(HttpServletRequest request,Model model){
        String shortUrlId=request.getAttribute("shortUrlId").toString();
        model.addAttribute("shortUrlId",shortUrlId);
        return "viewPwd";
    }

    @RequestMapping("VerifyPwd")
    @ResponseBody
    public String verifyPwd(String viewPwd,String shortUrlId){
        Surl surl=surls.findByShortId(shortUrlId);
        JSONObject json=new JSONObject();
        if(surl.getViewPwd().equals(viewPwd)){
            surls.updateShortUrl(shortUrlId);
            json.put("flag",true);
            json.put("longUrl",surl.getLongUrl());
        }else {
            json.put("flag",false);
        }
        return json.toJSONString();
    }


}
