package com.example.dockerjd.passwordService;

import org.springframework.stereotype.Service;

/**
 * @Author: permission
 * @Description:
 * @Date: Create in 20:06 2019/10/28
 * @Modified By:
 */
@Service
public class encryptByCaeserServer {
    /* 作用：加密   */
    public String encrypt(String plaintext){
        char[] chars = new char[plaintext.length()];//字符暂存数组
        for (int i = 0; i < plaintext.length(); i++) {
            chars[i] = (char)((int) plaintext.charAt(i)+3);
        }
        return new String(chars);
    }

    /* 作用：解码   */
    public String encode(String caeserPassword){
        char[] chars = new char[caeserPassword.length()];//字符暂存数组
        for (int i = 0; i < caeserPassword.length(); i++) {
            chars[i] = (char)((int) caeserPassword.charAt(i)-3);
        }
        return new String(chars);
    }
}
