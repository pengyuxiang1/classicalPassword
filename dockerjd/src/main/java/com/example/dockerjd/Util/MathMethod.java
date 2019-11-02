package com.example.dockerjd.Util;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
public class MathMethod {
    public static void main(String args[]){
        double b [][] = {{8,6,9},{6,9,5},{5,8,4}};
        //将数组转化为矩阵
        RealMatrix matrix = new Array2DRowRealMatrix(b);
        System.out.println("创建的数组为：\t"+matrix);
        //矩阵的乘法
//        double testmatrix[][] = {{0},{1},{2}};
        double testmatrix[][] = {{69,70,71}};
        RealMatrix testmatrix1 = new Array2DRowRealMatrix(testmatrix);
        RealMatrix testmatrix2 = new Array2DRowRealMatrix(b);
        System.out.println("两个矩阵相乘后的结果为：\t"+testmatrix1.multiply(testmatrix2));
//        //矩阵的转置
//        System.out.println("转置后的矩阵为：\t"+testmatrix1.transpose());
//        //矩阵求逆
        RealMatrix inversetestMatrix = inverseMatrix(testmatrix2);
        System.out.println("逆矩阵为：\t"+inversetestMatrix);
        RealMatrix testmatrix3 = new Array2DRowRealMatrix(new double[][]{{1270,1543,1201}});
        System.out.println("两个矩阵相乘后的结果为：\t"+testmatrix3.multiply(inversetestMatrix) );
//        //矩阵转化为数组 getdata
//        double matrixtoarray[][]=inversetestMatrix.getData();
//        System.out.println("数组中的某一个数字为：\t"+matrixtoarray[0][1]);
    }

    //求逆函数
    public static RealMatrix inverseMatrix(RealMatrix A) {
        RealMatrix result = new LUDecomposition(A).getSolver().getInverse();
        return result;
    }
}

