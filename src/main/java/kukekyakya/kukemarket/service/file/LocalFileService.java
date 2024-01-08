package kukekyakya.kukemarket.service.file;

import kukekyakya.kukemarket.exception.FileUploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class LocalFileService implements FileService {

    //파일 위치 application-local.yml 을 통해 주입
    @Value("${upload.image.location}")
    private String location; // 1

    //디렉토리 생성
    @PostConstruct
    void postConstruct() { // 2
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @Override
    public void upload(MultipartFile file, String filename) { // 3
        try {
            file.transferTo(new File(location + filename));
        } catch (IOException e) {
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(String filename) {

    }
}