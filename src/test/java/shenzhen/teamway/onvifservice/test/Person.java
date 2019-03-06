package shenzhen.teamway.onvifservice.test;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-05 10:18
 **/
public class Person {

    private String name = "a";
    private int age = 0;
    Boy boy;

    public Person() {
    }

    public Person(Boy boy) {
        this.boy = boy;
        name = boy.getName();
        age = boy.getAge();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", boy=" + boy +
                '}';
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner=new Scanner("aa");
        final String s1 = scanner.toString();
        System.out.println(s1);

    }
}