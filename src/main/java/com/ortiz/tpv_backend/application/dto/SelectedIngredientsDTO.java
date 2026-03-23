package com.ortiz.tpv_backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectedIngredientsDTO {

    @Builder.Default
    private List<String> included = new ArrayList<>();

    @Builder.Default
    private List<String> additional = new ArrayList<>();

    public Map<String, List<String>> toMap() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        result.put("included", included == null ? List.of() : List.copyOf(included));
        result.put("additional", additional == null ? List.of() : List.copyOf(additional));
        return result;
    }

    public static SelectedIngredientsDTO fromMap(Map<String, List<String>> source) {
        if (source == null || source.isEmpty()) {
            return SelectedIngredientsDTO.builder().build();
        }
        return SelectedIngredientsDTO.builder()
            .included(source.getOrDefault("included", List.of()))
            .additional(source.getOrDefault("additional", List.of()))
            .build();
    }
}
