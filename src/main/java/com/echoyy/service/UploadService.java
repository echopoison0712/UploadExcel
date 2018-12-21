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

    boolean getInformation(String name, MultipartFile file);
}
