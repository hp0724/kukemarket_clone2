package kukekyakya.kukemarket.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LocalFileServiceTest {
    LocalFileService localFileService = new LocalFileService();
    String testLocation = new File("C://Users//suha hwang//Pictures//test").getAbsolutePath() + "/"; // 1

    //localfileService 에 location 주입
    @BeforeEach
    void beforeEach() throws IOException { // 2
        ReflectionTestUtils.setField(localFileService, "location", testLocation);
        //테스트 시작전에 모든 파일 제거
        FileUtils.cleanDirectory(new File(testLocation));
    }

    @Test
    void uploadTest() { // 3
        // given
        MultipartFile file = new MockMultipartFile("suhaFile", "suhaFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "suhaFile.txt";

        // when
        localFileService.upload(file, filename);

        // then
        assertThat(isExists(testLocation + filename)).isTrue();
    }

    boolean isExists(String filePath) {
        return new File(filePath).exists();
    }
}
