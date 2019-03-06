package shenzhen.teamway.onvifservice.test;

import java.util.Optional;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-05 10:18
 **/
public class Boy {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boy(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args) {
        Boy boy=null;
        Optional.ofNullable(boy);
    }
}