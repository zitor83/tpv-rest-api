package com.ortiz.tpv_backend;

import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TpvBackendApplicationTests {

	@MockBean
	private ProductRepositoryPort productRepositoryPort;

	@Test
	void contextLoads() {
	}

}
