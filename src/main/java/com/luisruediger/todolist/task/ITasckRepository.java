package com.luisruediger.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITasckRepository extends JpaRepository<TaskModel, UUID> {
  
}
