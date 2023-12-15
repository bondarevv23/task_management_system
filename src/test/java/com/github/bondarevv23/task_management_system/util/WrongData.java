package com.github.bondarevv23.task_management_system.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WrongData {
    @JsonProperty("value")
    private String value;

    @JsonProperty("another_one_value")
    private Integer someField = 10;

    public WrongData(String value) {
        this.value = value;
    }
}
