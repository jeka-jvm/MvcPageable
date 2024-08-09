package org.example.mvcpageable.repository;

import org.example.mvcpageable.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorRepository extends JpaRepository<Author, Long> {
}
