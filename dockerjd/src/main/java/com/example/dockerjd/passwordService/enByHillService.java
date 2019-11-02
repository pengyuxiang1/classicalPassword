package com.example.dockerjd.passwordService;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @Author: permission
 * @Description:
 * @Date: Create in 20:30 2019/10/28
 * @Modified By:
 */
@Service
public class enByHillService {

    public double[][] key={{8,6,9},{6,9,5},{5,8,4}};

    /* 作用：对三位明文加密  */
    private double[][] encrypt(double[][] b) {
        RealMatrix matrixb = new Array2DRowRealMatrix(b);
        RealMatrix matrixkey = new Array2DRowRealMatrix(key);
        double[][] matrixtoarray = matrixb.multiply(matrixkey).getData();
        return matrixtoarray;
    }

    /* 作用：字符串分割为三位   */
    public String[] getPlaintexts(String plaintext){
        String[] plaintexts;
        if(plaintext.length()/3==0){
            plaintexts=new String[1];
        }else if(plaintext.length()%3==0){
            plaintexts=new String[plaintext.length()/3];
        }else {
            plaintexts=new String[plaintext.length()/3+1];
        }

        if(plaintext.length()>=3){
            int i=0,j=0;
            for(;i<plaintext.length()/3;i++){
                plaintexts[i]=plaintext.substring(j,j+3);
                j+=3;
            }
            if (plaintext.substring(j).length()==1){
                plaintexts[i]=plaintext.substring(j)+"xx";
            }
            if (plaintext.substring(j).length()==2){
                plaintexts[i]=plaintext.substring(j)+"x";
            }
        }else{
            if (plaintext.length()==1){
                plaintexts[0]=plaintext+"xx";
            }
            if (plaintext.length()==2){
                plaintexts[0]=plaintext+"x";
            }
        }
        return plaintexts;
    }

    /* 作用：String转为double【】【】   */
    public double[][] StringtoDouble(String a){
        System.out.println(a);
        char[] chars = new char[a.length()];//字符暂存数组
        for (int i = 0; i < a.length(); i++) {
            chars[i] = a.charAt(i);
        }
        double[][] b = new double[1][a.length()];//明文数组
        for (int i = 0; i < a.length(); i++) {
            int parse1=Integer.valueOf(a.charAt(i));
            b[0][i]=parse1;
        }
        return b;
    }

    /* 作用：double二维数组转化一个字符串   */
    public  String doubleToString(double[][] array) {
        StringBuffer plaintext=new StringBuffer();
        System.out.println(array.length+"列长："+array[0].length);
        for (int t = 0; t < array.length; t++) {
            for (int y = 0; y < array[t].length; y++) {
                int ac= (int) (array[t][y]+0.001);
                plaintext.append(ac+",");
            }
        }
        return plaintext.toString();//问题1不确定会不会出现toString变成的是一串地址吗  没问题
    }

    /* 作用：长加密   */
    public String hillCode(String plaintext){
        /**
         *  @Author: permission
         *  @Description: 输入明文，即可获得密文
         *  @Date: 2019/10/31 23:30
         *  @Param [plaintext]
         *  @Return: java.lang.String
        */
        String ciphertext="";
        double[][] ciphertexts;
        if(plaintext.length()/3==0){
            ciphertexts=new double[1][3];
        }else if(plaintext.length()%3==0){
            ciphertexts=new double[plaintext.length()/3][3];
        }else {
            ciphertexts=new double[plaintext.length()/3+1][3];
        }
        String[] plaintexts=getPlaintexts(plaintext);
        for(int i=0;i<plaintexts.length;i++){
            double[][] d=StringtoDouble(plaintexts[i]);
            double[][] d2=encrypt(d);
            ciphertexts[i]=d2[0];//i行3列，一行是一个密文串
        }
        ciphertext=doubleToString(ciphertexts);
        return ciphertext;
    }

    /* 作用：String[]数组转化为double[][]   */
    public double[][]  StringToDouble(String[] mingwen){
        double[][] d;
        if(mingwen.length/3==0){
            d=new double[1][3];
        }else if(mingwen.length%3==0){
            d=new double[mingwen.length/3][3];
        }else {
            d=new double[mingwen.length/3+1][3];
        }
        for(int i=0;i<d.length;i++){
            for(int j=0;j<3;j++){
                d[i][j]=Integer.valueOf(mingwen[i*3+j]);
            }
        }
        return d;
    }

    /* 作用：翻译明文   */
    public String translate(double[][] array){
        StringBuffer plaintext=new StringBuffer();
        for (int t = 0; t < array.length; t++) {
            for (int y = 0; y < array[t].length; y++) {
                int ac= (int) (array[t][y]+0.001);
                plaintext.append((char)ac);
            }
        }
        return plaintext.toString();//问题1不确定会不会出现toString变成的是一串地址吗  没问题
    }

    /* 作用：对三位密文解码   */
    public double[][] decrypt(double[][] miwen) {
        RealMatrix key_1 = inverseMatrix(new Array2DRowRealMatrix(key));
        RealMatrix matrixmiwen;
        double[][] mingwen;
        double[][] mingwens=new double[miwen.length][3];
        double[][] parse=new double[1][3];;
        for(int i=0;i<miwen.length;i++){
            parse[0]=miwen[i];
            matrixmiwen = new Array2DRowRealMatrix(parse);
            mingwen = matrixmiwen.multiply(key_1).getData();
            mingwens[i]=mingwen[0];
        }
        return mingwens;
    }

    /* 作用：长解码   */
    public String encode(String ciphertext){
        /**
         *  @Author: permission
         *  @Description: 输入密文，即可获得明文
         *  @Date: 2019/10/31 23:31
         *  @Param [ciphertext]
         *  @Return: java.lang.String
        */
        String plaintext="";
        String[] ciphertexts = ciphertext.split(",");
        double[][] d=StringToDouble(ciphertexts);//String[]数组转化为double[][]
        double[][] mingwen=decrypt(d);//三位一组调用decrypt解密得到明文//返回的明文double[][]再次成为我的数据结构：即i行3列的形式
        plaintext=translate(mingwen);//按顺序遍历明文double【】【】强转为char【】
        //char输出为原文。
        return plaintext;
    }

    /* 作用：key求逆   */
    public static RealMatrix inverseMatrix(RealMatrix A) {
        RealMatrix result = new LUDecomposition(A).getSolver().getInverse();
        return result;
    }
}


