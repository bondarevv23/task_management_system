package com.github.bondarevv23.task_management_system.generator;

import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ResponseGenerator {
    public static TokenResponse getRightTokenResponse() {
        return TokenResponse.builder().accessToken("right-access-token").build();
    }

    public static Page<Task> getPageTask() {
        return new PageImpl<>(
                IntStream.range(1, 6).mapToObj(RepositoryGenerator::getTaskWithId).collect(Collectors.toList()),
                PageRequest.of(0, 20),
                5);
    }
}
