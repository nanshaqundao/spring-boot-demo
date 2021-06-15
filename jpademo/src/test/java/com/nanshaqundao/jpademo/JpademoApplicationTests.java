package com.nanshaqundao.jpademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class JpademoApplicationTests {
	@Autowired
	private GenericEntityRepository genericEntityRepository;


	@Test
	@Transactional
	public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
		GenericEntity genericEntity = genericEntityRepository.save(new GenericEntity("test"));
		GenericEntity foundEntity = genericEntityRepository.getOne(genericEntity.getId());

		assertNotNull(foundEntity);
		System.out.println(genericEntity);

		assertEquals(genericEntity.getValue(), foundEntity.getValue());
	}
}
