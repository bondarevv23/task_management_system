package com.github.bondarevv23.task_management_system.model.mapper;

import com.github.bondarevv23.task_management_system.model.User;
import com.github.bondarevv23.task_management_system.model.api.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", expression = "java(user.getId().toString())")
    UserDTO userToUserDTO(User user);
}
