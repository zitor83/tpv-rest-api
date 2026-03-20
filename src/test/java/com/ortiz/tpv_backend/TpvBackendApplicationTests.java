package com.ortiz.tpv_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;

@SpringBootTest
class TpvBackendApplicationTests {

	@MockBean
	private ProductRepositoryPort productRepositoryPort;

	@Test
	void contextLoads() {
	}

}
