package com.hancom.hanzari.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.Shape;


@Repository
public interface ShapeRepository extends JpaRepository<Shape, String> {

}
