package com.trodix.todoapi.repository;

import java.util.UUID;

import com.trodix.todoapi.entity.Todo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<Todo, UUID> {}