package com.echoyy.service;

import com.echoyy.pojo.Knowledge;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName UploadService
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午2:57
 * @Version 1.0
 */
public interface UploadService {
    int add(Knowledge knowledge);

    void delete(int id);

    Knowledge get(int id);

    int update(Knowledge knowledge);

    List<Knowledge> list();

    boolean upload(String name, MultipartFile file);
}
