package com.nanshaqundao.jpademo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericEntityRepository
        extends JpaRepository<GenericEntity, Long> { }
