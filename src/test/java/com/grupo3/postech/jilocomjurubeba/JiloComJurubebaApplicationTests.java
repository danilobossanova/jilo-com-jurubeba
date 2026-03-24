package com.grupo3.postech.jilocomjurubeba;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.grupo3.postech.jilocomjurubeba.infrastructure.security.TestSecurityConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class JiloComJurubebaApplicationTests {

    @Test
    void contextLoads() {}
}
