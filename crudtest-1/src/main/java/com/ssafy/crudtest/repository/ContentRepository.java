package com.ssafy.crudtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.crudtest.entity.Content;

public interface ContentRepository extends JpaRepository<Content,Integer>{

	public List<Content> findTop1000ByOrderByUidDesc();
}
