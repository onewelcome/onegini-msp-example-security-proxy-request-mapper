package com.onegini.examples;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

import com.onegini.examples.service.TokenValidationResultMappingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = AFTER_CLASS)
@SpringBootTest(classes = { ExampleRequestMapperApplication.class })
class SpringContextTest {

  @Resource
  private TokenValidationResultMappingService service;

  @Test
  void should_injected() {
    assertThat(service).isNotNull();
  }

}