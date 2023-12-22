package com.github.bondarevv23.task_management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User {
    @Id
    private UUID id;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="author", cascade = CascadeType.REMOVE)
    private List<Task> createdTasks;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="performer", cascade = CascadeType.REMOVE)
    private List<Task> tasksToPerform;
}
