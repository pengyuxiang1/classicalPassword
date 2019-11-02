package com.example.dockerjd.Util;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.Scanner;
/**
 * @Author: permission
 * @Description:
 * @Date: Create in 15:23 2019/10/30
 * @Modified By:
 */


public class Hill {
    public static char[] zimubiao = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static String[] zhihuanbiao = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"
            , "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"};
    public static void main(String[] args) throws Exception {
        while (true) {
            //获得加密序列（只能够4位，差评，要自己写一个支持多位的了）
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入要加密的序列(4位)");
            String a = scanner.next();//获得明文
            int[] intToKey=new int[a.length()];
            if (a.length()==3) {
                char[] chars = new char[a.length()];
                for (int i = 0; i < a.length(); i++) {
                    chars[i] = a.charAt(i);
                }
                double[][] b = new double[1][a.length()];
                int[] change=new int[a.length()];
                for (int i = 0; i < a.length(); i++) {
                    int parse1=Integer.valueOf(a.charAt(i));
                    intToKey[i]=parse1-65;
                    System.out.println(intToKey[i]);
                    change[i]=intToKey[i];
                    while(change[i]<0){
                        change[i]+=26;
                    }
                    while(change[i]>=26){
                        change[i]-=26;
                    }
                    b[0][i]=change[i];
                }
                System.out.println("明文序列对应的数字矩阵是：");
                printArray(b);
                System.out.println();
                //构建四维的加密密钥
                double[][] key = {
                        {8, 6, 9},
                        {6, 9, 5},
                        {5, 8, 4},
                };
                System.out.println("选择的密钥矩阵是：");
                printArray(key);
                //加密得到密文
                double[][] miwen = encrypt(b, key);
                System.out.println("密文矩阵序列为：");
                printArray(miwen);//理论上应该是一个一维数组。
                System.out.println();

                System.out.println("解密后，明文矩阵序列为：");
                double[][] mingwen = decrypt(miwen, key);
                printArray(mingwen);//理论上应该是一个字符串

                //数字转化为a-z
                char[] chars1=new char[a.length()];
                for (int i = 0; i < a.length(); i++) {
                    for (int j = 0; j < zimubiao.length; j++) {
                        int ab=(int)(mingwen[0][i]+0.0001);
                        if(ab >=26) {
                            ab-=26;
                        }
                        if (ab == Integer.parseInt(zhihuanbiao[j])) {
                            chars1[i]=zimubiao[j];
                        }
                    }
                }
                char[] chars2=new char[a.length()];
                int parse=0;
                for (int i = 0; i < a.length(); i++) {
//                    System.out.println(intToKey[i]);
//                    System.out.println(Integer.valueOf(chars1[i]));
                    if(intToKey[i]>=0){
                        parse=Integer.valueOf(chars1[i])+(intToKey[i]/26)*26;
                    }else{
                        parse=Integer.valueOf(chars1[i])-(1+intToKey[i]/26)*26;
                    }
                    chars2[i]=(char)parse;
                }
                System.out.println("解密后：");
                System.out.println(String.valueOf(chars2));
            }else {
                System.out.println("长度不正确请重新输入");
            }
        }
    }

    /**
     * @return double[][]
     * @author Fever1
     * @Description 解密运算
     * @Date 12:33 2019/1/7
     * @Param [miwen, key]
     **/
    private static double[][] decrypt(double[][] miwen, double[][] key) {
        RealMatrix key_1 = inverseMatrix(new Array2DRowRealMatrix(key));
        System.out.println("逆矩阵为：\t"+key_1);
        RealMatrix matrixmiwen = new Array2DRowRealMatrix(miwen);
        double[][] mingwen = matrixmiwen.multiply(key_1).getData();
        System.out.println("两个矩阵相乘后的结果为：\t"+matrixmiwen.multiply(key_1));
        floodMod(mingwen);//对明文向量求模，限制在0-26之间

        return mingwen;
    }

    /**
     * @return double[][]
     * @author Fever1
     * @Description 加密运算
     * @Date 12:34 2019/1/7
     * @Param [b, key]
     **/
    private static double[][] encrypt(double[][] b, double[][] key) {
        RealMatrix matrixb = new Array2DRowRealMatrix(b);
        RealMatrix matrixkey = new Array2DRowRealMatrix(key);
        double[][] matrixtoarray = matrixb.multiply(matrixkey).getData();
        floodMod(matrixtoarray);//对密文向量求模，限制在0-26之间
        return matrixtoarray;
    }

    /**
     * @return void
     * @author Fever1
     * @Description 求包括负数与正数的Mod运算
     * @Date 12:33 2019/1/7
     * @Param [matrixtoarray1]
     **/
    private static void floodMod(double[][] matrixtoarray1) {
        for (int i = 0; i < matrixtoarray1.length; i++) {
            for (int j = 0; j < matrixtoarray1[i].length; j++) {
                if (matrixtoarray1[i][j]>= 0) {
                    System.out.println(matrixtoarray1[i][j]);
                    while (matrixtoarray1[i][j] >= 26) {//大于26的结果，将其求模到0-26
                        matrixtoarray1[i][j] = Math.round(matrixtoarray1[i][j] - 26);
                    }
                } else {
                    while (matrixtoarray1[i][j] < 0) {//小于26的结果，将其求模到0-26
                        matrixtoarray1[i][j] = Math.round(matrixtoarray1[i][j] + 26);
                    }
                }
            }
        }
    }

    /**
     * @return void
     * @author Fever1
     * @Description 遍历打印数组
     * @Date 22:47 2019/1/6
     * @Param [array]
     **/
    private static void printArray(double[][] array) {
        for (int t = 0; t < array.length; t++) {
            for (int y = 0; y < array[t].length; y++) {
                int ac= (int) (array[t][y]+0.001);
                System.out.print(ac + "\t");
            }
            System.out.println();
        }
    }

    /**
     * @return org.apache.commons.math3.linear.RealMatrix
     * @author Fever1
     * @Description 矩阵求逆
     * @Date 13:46 2018/12/26
     * @Param [A]
     **/
    public static RealMatrix inverseMatrix(RealMatrix A) {
        RealMatrix result = new LUDecomposition(A).getSolver().getInverse();
        return result;
    }
}

