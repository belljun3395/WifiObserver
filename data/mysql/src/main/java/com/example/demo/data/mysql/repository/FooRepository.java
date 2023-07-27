package com.example.demo.data.mysql.repository;

import com.example.demo.data.mysql.entity.FooEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FooRepository extends JpaRepository<FooEntity, Long> {}
