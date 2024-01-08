package kukekyakya.kukemarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.image.location")
    private String location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // /image/ 접두 경로가 설정되어 있으면
        registry.addResourceHandler("/image/**")
                // 파일 시스템의 location 경로에서 파일에 접근한다.
                .addResourceLocations("file"+location)
                //업로드된 각각의 이미지는 고유한 이름
                //자원에 접근할때마다 캐시된 자원을 이용
                //1시간이 지나면 캐시는 만료되고 다시 요청
                .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic());
    }
}
