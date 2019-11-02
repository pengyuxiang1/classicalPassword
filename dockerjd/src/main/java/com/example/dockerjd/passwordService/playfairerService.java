package com.example.dockerjd.passwordService;

import org.springframework.stereotype.Service;

/**
 * @Author: permission
 * @Description:
 * @Date: Create in 0:07 2019/10/30
 * @Modified By:
 */
@Service
public class playfairerService {
    private char[][] key ={{'p','e','n','g','y'},
                            {'u','x','i','a','b'},
                            {'q','w','r','t','o'},
                            {'s','d','f','h','j'},
                            {'k','l','c','v','m'}};//去z版本

    /* 作用：置换   */
    private char[] change(char a,char b){
        char[] returnWord=new char[2];
        int a1=0,b1=0;
        int a2=0,b2=0;
        for(int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if(a==key[i][j]){
                    a1=i;b1=j;
                }
                if(b==key[i][j]){
                    a2=i;b2=j;
                }
            }
        }//循环遍历，确定ij位置
        if(a1==a2){
            returnWord[0]=key[a1][(b1+1)%5];
            returnWord[1]=key[a2][(b2+1)%5];
        }else if(b1==b2){
            returnWord[0]=key[(a1+1)%5][b1];
            returnWord[1]=key[(a2+1)%5][b2];
        }else{
            returnWord[0]=key[a2][b1];
            returnWord[1]=key[a1][b2];
        }
        //比较两个word各自的ij，if（i1=i2）那么赋值给对应位置1/2的returnword的值是returnword【1】=key【(i+1)%5】【j】;
        //if（j1=j2）,那么对应的是rewornword【1】=key【i】【（j+1）%5】；最后，两个if都没有执行，那么对应的是（交换x，而不是对x进行计算）rewornword【1】=key【i2】【j】，rewornword【1】=key【i1】【j】
        return returnWord;
    }



    /* 作用：整理明文   */
    private char[] tidyPlaintext(String plaintext){
        StringBuffer stringBuffer=new StringBuffer(plaintext);
        for(int i=0;i<stringBuffer.length();i+=2){
            if(i+1==stringBuffer.length()){//消除临界状态。处理数组等数据必备考虑点。
                break;
            }
            if(stringBuffer.charAt(i)==stringBuffer.charAt(i+1)){
                stringBuffer.insert(i+1,'x');//插入到指定位置，而不是指定位置后方
            }
        }
        if(stringBuffer.length()%2!=0){
            stringBuffer.append('x');
        }
        char[] returnWord=new char[stringBuffer.length()];
        for (int i = 0; i < stringBuffer.length(); i++) {
            returnWord[i] = stringBuffer.charAt(i);
        }
        return returnWord;
    }

    /* 作用：加密   */
    public String playfair(String plaintext){
        //整理明文，长度不是偶数就补上一个x，一组两个相同就补上x。分两步实现。最后返回字符化char【】数组，
        //二元替代函数，传入两个字符char【】，传出两个字符char【】，不够偶数补充x
        // 存储函数，将返回的数据循环存储。
        String caeserPassword="";
        char[] chars=tidyPlaintext(plaintext);
        char[] parse=new char[chars.length];
        for(int i=0;i<chars.length;i+=2){//明文处理了，必定是偶数，所以不用考虑临界基数的时候产生的越界情况
            char[] a=change(chars[i],chars[i+1]);//暂存返回结果到a
            parse[i]=a[0];parse[i+1]=a[1];//结果真正存入暂存char数组中
        }
        caeserPassword=new String(parse);
        return caeserPassword;
    }

    /* 作用：两个char进行解码   */
    private  char[] reBack(char a,char b ){
        char[] returnWord=new char[2];
        int a1=0,b1=0;
        int a2=0,b2=0;
        for(int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if(a==key[i][j]){
                    a1=i;b1=j;
                }
                if(b==key[i][j]){
                    a2=i;b2=j;
                }
            }
        }
        if(a1==a2){
            returnWord[0]=key[a1][(b1-1)%5];
            returnWord[1]=key[a2][(b2-1)%5];
        }else if(b1==b2){
            returnWord[0]=key[(a1+4)%5][b1];
            returnWord[1]=key[(a2+4)%5][b2];
        }else{
            returnWord[0]=key[a2][b1];
            returnWord[1]=key[a1][b2];
        }
        return returnWord;
    }
    
    /* 作用：密文处理   */
    private String tidyCiphertext(String Ciphertext){
        //没有完善，出现了全xxx的符号，无法处理，同时，如果是3个以上的x连续，那么，规律是每多一个x，传入的Ciphertext就多两个x可以利用这个规律，判定多少个连续的x，
        // 明文有3个连续x对应密文是4个x，明文4个x对应密文是6个x，密文5个对应8个x。即明文为x 对应密文（x-2）*2+2（x>2时）.

        System.out.println(Ciphertext);
        int[] a= new int[Ciphertext.length()];int j=0;//准备好表的数据结构，这里用数组代替
        StringBuffer stringBuffer=new StringBuffer(Ciphertext);
        for(int i=1;i<stringBuffer.length();i++){//buffer遍历，找到x并且利用数组记录位置，将0和最后一个除外
            if(i==Ciphertext.length()-1){
                break;
            }
            else if (stringBuffer.charAt(i)=='x'){
                a[j]=i;
                j++;
            }
        }

        for(int z=0;z<j;z++){//循环对x前后判断，一样就删除，不一样就保留
            if(stringBuffer.charAt(a[z]-1)==stringBuffer.charAt(a[z]+1)){
                stringBuffer.deleteCharAt(a[z]);
                for(int k=1;k<j-z;k++){
                    a[z+k]--;
                }
            }
        }
        return stringBuffer.toString();
    }
    /* 作用：解密   */
    public String encode(String ciphered){
        if(ciphered.length()%2!=0){
            return "密文错误，应该是偶数密文，不要自己乱打密文";
        }
        String plaintext ="";
        char[] parse=new char[ciphered.length()];
//        char[] chars=tidyPlaintext(ciphered);//密文一定是偶数，没有重复单词在同一组，所以不用处理了,只要转化为char【】就好了
        char[] chars=tidyPlaintext(ciphered);int h=0; //准备转换字符串的数据结构。一个表来装，一个计数位
        for (char t:ciphered.toCharArray()
             ) {
            chars[h]=t;
            h++;
        }
        for(int i=0;i<ciphered.length();i+=2){
            char[] a=reBack(chars[i],chars[i+1]);//暂存返回结果到a
            parse[i]=a[0];parse[i+1]=a[1];//结果真正存入暂存char数组中
        }
        plaintext=new String(parse);
        return tidyCiphertext(plaintext);
    }

//    public static void main(String[] args) {
//        playfairerService playfairerService=new playfairerService();
//        String a=playfairerService.playfair("hello");
//        System.out.println(a);
//        String b=playfairerService.encode(a);
//        System.out.println(b);
//    }
}
