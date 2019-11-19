package com.wlkg.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


public class test {
    public static void main(String[] args) {
        double a = 1;
        List<Double> list = new ArrayList<>();
        list.add(a);
        list.add(a);
        for(int i = 2 ; i < 1001; i ++ ){
            list.add(list.get(i-1)+list.get(i-2));

        }

        list.forEach(s-> System.out.println(s));
        /*double sum = 0;
        for(int i = 0; i < list.size(); i++ ){
            sum += list.get(i);
        }
        System.out.println(sum);*/
    }


}
