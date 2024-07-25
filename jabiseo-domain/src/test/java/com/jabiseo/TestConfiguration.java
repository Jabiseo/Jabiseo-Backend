package com.jabiseo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

// domain 모듈은 @ComponentScan이 없으므로 @DataJpaTest, @SpringBootTest를 사용하지 못한다. @ComponentScan을 추가하기 위한 클래스이다.
@SpringBootApplication
public class TestConfiguration {
}
