package com.uem.sd.trabalho.to_do_list.repository;

import com.uem.sd.trabalho.to_do_list.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findAllByUserId(Long userId);
}
