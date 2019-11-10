package io.gomk.model.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TargetMapDTO {
    private List<Map<String, Object>> nodes;
    private List<Map<String, Object>>  edges;
}
