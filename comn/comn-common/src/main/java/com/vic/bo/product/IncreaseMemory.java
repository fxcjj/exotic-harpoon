package com.vic.bo.product;

import java.util.ArrayList;
import java.util.List;

public class IncreaseMemory {

    public String name;
    public String name1;
    public String name2;
    public String name3;
    public String name4;
    public String name5;
    public String name6;
    public String name7;
    public String name8;
    public String name9;
    public String name10;


    public static void main(String[] args) throws InterruptedException {
        List<IncreaseMemory> list = new ArrayList<>();
        /*for(int i = 0; i < 100000000; i++) {
            IncreaseMemory in = new IncreaseMemory();
            list.add(in);
        }*/


        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        System.out.println("maxMemory: " + maxMemory/(1024*1024)); //
        System.out.println("totalMemory: " + totalMemory/(1024*1024));
        System.out.println("freeMemory: " + freeMemory/(1024*1024));




    }
}
