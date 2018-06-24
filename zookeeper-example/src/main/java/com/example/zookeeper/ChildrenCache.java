package com.example.zookeeper;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ChildrenCache {

    List<String> toPrecess;

    public List<String> removeAndSet(List<String> children) {
        Optional<String> optionalS = toPrecess.stream().filter(s->!children.contains(s)).findAny();

        return null;
    }
}
