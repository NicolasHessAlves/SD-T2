package com.uem.sd.trabalho.to_do_list.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_to_do")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="description", nullable = false, length = 512)
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "done", nullable = false)
    private boolean done;
}
