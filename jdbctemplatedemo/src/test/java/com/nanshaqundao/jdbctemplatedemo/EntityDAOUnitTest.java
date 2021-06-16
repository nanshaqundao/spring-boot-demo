package com.nanshaqundao.jdbctemplatedemo;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EntityDAOUnitTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Test
    public void whenMockJdbcTemplate_thenReturnCorrectEntityCount() {
        EntityDAO entityDAO = new EntityDAO();
        ReflectionTestUtils.setField(entityDAO, "jdbcTemplate", jdbcTemplate);
        Mockito.when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ENTITY", Integer.class))
                .thenReturn(4);

        assertEquals(4, entityDAO.getCountOfEntity());
    }
}
