package com.trodix.exempleapp.repository;

import java.util.UUID;

import com.trodix.exempleapp.entity.Todo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<Todo, UUID> {}