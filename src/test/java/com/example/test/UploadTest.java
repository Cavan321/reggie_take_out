package com.example.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author Cavan
 * @date 2023-04-01
 * @qq 2069543852
 */
public class UploadTest {

    @Test
    public void test01(){

        String fileName = "nocin.jpg";
        int indexOf = fileName.lastIndexOf(".");
        String suffix = fileName.substring(indexOf);

        String prefix = UUID.randomUUID().toString().replace("-","");



        System.out.println(prefix+suffix);


    }

}
