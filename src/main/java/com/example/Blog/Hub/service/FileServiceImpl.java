package com.example.Blog.Hub.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(String path, MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        int i = name.lastIndexOf(".");
        String s = name.substring(i);
        String str = UUID.randomUUID()+s;

        //fullPath
        String filePath = path+ File.separator+str;

        // create folder if not created
        File f = new File(path);
        if (!f.exists()){
            f.mkdir();
        }
        // File Copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return str;
    }

    @Override
    public InputStream getImage(String path, String filename) throws FileNotFoundException {
        String fullPath = path+File.separator+filename;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
