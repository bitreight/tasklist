package com.bitreight.tasklist.service;

import java.io.IOException;

public interface ImageService {

    String uploadImage(byte[] imageBytes) throws IOException;
}
