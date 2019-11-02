package com.example.dockerjd.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.dockerjd.passwordService.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: permission
 * @Description:
 * @Date: Create in 20:00 2019/10/28
 * @Modified By:
 */
@Controller
@RequestMapping("/encryption")
public class encryptionController {
    @Autowired
    private encryptByCaeserServer encryptByCaeserServer;
    @Autowired
    private enByHillService enByHillService;
    @Autowired
    private playfairerService playfairerService;
    @RequestMapping(value="/index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        return new ModelAndView("/mi/try");
    }
    //request方式获得参数

    @RequestMapping("/caeser")
    public String encryptionByCaeser(Model model,@RequestParam String front){
        String ciphertext=encryptByCaeserServer.encrypt(front);
        model.addAttribute("result",ciphertext);
        return "/mi/try";//返回路径文件也可以，不带model也可以，好奇怪啊。
    }
    //model+string方式
    //@RequestParam方式获得参数，三种方式，反射，@RequestParam（“属性名”），对象反射接收多个属性

    @RequestMapping("/caeser/encode")
    public ModelAndView encodeCaeser(@RequestParam String front){
        ModelAndView modelAndView=new ModelAndView("/mi/try");
        String ciphertext=encryptByCaeserServer.encode(front);
        modelAndView.addObject("result",ciphertext);
        return modelAndView;
    }
    //modelAndView直接插入法

    @RequestMapping("/hill")
    public String encryptionByHill(Model model,@RequestParam String front){
        String ciphertext=enByHillService.hillCode(front);
        model.addAttribute("result",ciphertext);
        return "/mi/try";
    }
    //跳转方式

    @RequestMapping("/hill/encode")
    public String encodeHill(Model model,@RequestParam String front){
        String plaintext=enByHillService.encode(front);
        model.addAttribute("result",plaintext);
        return "/mi/try";
    }
    //@ModelAttribute方法直接让所有的view都插入此model，此方法不讲

    @RequestMapping("/playfair")
    public String encryptionByPlayfair(Model model,@RequestParam String front){
        String ciphertext=playfairerService.playfair(front);
        model.addAttribute("result",ciphertext);
        return "/mi/try";
    }

    @RequestMapping("/playfair/encode")
    public String encodePlayfair(Model model,@RequestParam String front){
        String plaintext=playfairerService.encode(front);
        model.addAttribute("result",plaintext);
        return "/mi/try";
    }

}
