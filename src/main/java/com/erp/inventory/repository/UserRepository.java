package com.erp.inventory.repository;

import com.erp.inventory.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByCompanyId(String companyId);

    Optional<User> findByEmailAndCompanyId(String email, String companyId);
}