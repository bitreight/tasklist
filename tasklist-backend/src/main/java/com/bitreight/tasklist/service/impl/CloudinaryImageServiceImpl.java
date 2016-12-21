package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.service.ImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@PropertySource("classpath:/config/cloudinary.properties")
public class CloudinaryImageServiceImpl implements ImageService {

    @Autowired
    private Environment environment;

    @Override
    public String uploadImage(byte[] imageBytes) throws IOException {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getRequiredProperty("cloud_name"),
                "api_key", environment.getRequiredProperty("api_key"),
                "api_secret", environment.getRequiredProperty("api_secret")));

        Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());

        return (String) uploadResult.get("url");
    }
}
