package com;

import com.zero.egg.DemoApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @ClassName Apptest
 * @Description TODO
 * @Author lyming
 * @Date 2018/11/2 2:11 PM
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class Apptest {

    @Before
    public void init() {
        System.out.println("-------------测试开始-------------");
    }

    @After
    public void after() {
        System.out.println("-------------测试结束--------------");
    }

    public static void main(String[] args) {
        SpringApplication.run(Apptest.class, args);
    }

}
