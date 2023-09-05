package com.example.Blog.Hub.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String upload(String path, MultipartFile file) throws IOException;
    InputStream getImage(String path, String filename) throws FileNotFoundException;
}
