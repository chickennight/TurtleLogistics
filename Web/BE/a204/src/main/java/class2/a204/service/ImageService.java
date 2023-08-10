package class2.a204.service;

import class2.a204.entity.Image;
import class2.a204.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;


    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;
        File dest = new File(filePath);

        // 디렉토리 생성
        if (!dest.getParentFile().exists())
            dest.getParentFile().mkdirs();

        file.transferTo(dest);
    }


    public ResponseEntity<UrlResource> downloadImage(Integer logNum) throws MalformedURLException {
        Optional<Image> image = imageRepository.findByLogNum(logNum);

        System.out.println(image);
        String contentType = "application/octet-stream";
        if (image.isPresent())
            contentType = image.get().getContentType();
        String logNumS = String.valueOf(logNum);
        Path file = Paths.get(uploadDir).resolve(logNumS + "." + contentType.split("/")[1]);
        UrlResource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable())
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
