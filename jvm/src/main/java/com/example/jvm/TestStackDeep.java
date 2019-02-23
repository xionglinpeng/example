package com.example.jvm;

public class TestStackDeep {

    private static int count = 0;

    public static void recersion(long a,long b,long c){
        long e=1,f=2,g=3,h=4,i=5,k=6,q=7,x=8,y=9,z=10;
        count++;
        recersion(a,b,c);
    }

    public static void recersion(){
        count++;
        recersion();
    }

    //指定的Java线程堆栈大小太小。指定至少180k
    public static void main(String[] args) {
        try {
            recersion(0L,0L,0L);
//            recersion();
        } catch (Throwable e) {
            System.out.println("deep of calling = "+count);
            e.printStackTrace();
        }

    }
}
