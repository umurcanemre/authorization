package com.umurcanemre.services.authorization.repository;

import org.springframework.data.repository.CrudRepository;

import com.umurcanemre.services.authorization.entity.ValidationCode;

public interface ValidationCodeRepository extends CrudRepository<ValidationCode, String>  {

}
