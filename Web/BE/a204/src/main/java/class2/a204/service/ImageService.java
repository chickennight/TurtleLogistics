package class2.a204.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;
        File dest = new File(filePath);
        file.transferTo(dest);
        return filePath;
    }

    public Resource downloadImage(String logNum) throws MalformedURLException {
        Path file = Paths.get(uploadDir).resolve(logNum);
        return new UrlResource(file.toUri());
    }
}
