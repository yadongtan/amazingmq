package com.yadong.amazingmq.utils;


public class NettyUtils {

    public static byte[] intToBytes(int a){
        byte[] ans = new byte[4];
        for(int i = 0; i < 4; i++)
            ans[3 - i] = (byte)(a >> (i * 8));//截断 int 的低 8 位为一个字节 byte，并存储起来
        return ans;
    }
    public static byte[] intToByteArray(int n) {
        /* byte                               0000 0000
         * short                    0000 0000 0000 0000
         * int  0000 0000 0000 0000 0000 0000 0000 0000
         */
        byte[] b = new byte[4];

        for (int i = 3, j = 0; i >= 0; i--, j++) {
            b[j] = (byte) ((n >> (i * 8)) & 0xff);
        }

        return b;
    }

    public static int bytesToInt(byte[] a){
        int ans=0;
        for(int i=0;i<4;i++){
            ans<<=8;
            ans|=(a[3-i]&0xff);
        }
        return ans;
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }


}
