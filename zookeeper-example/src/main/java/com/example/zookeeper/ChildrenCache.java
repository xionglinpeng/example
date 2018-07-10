package com.example.zookeeper;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ChildrenCache {

    List<String> toPrecess;

    public Set<String> removeAndSet(List<String> children) {
        Set<String> removeChildrens = toPrecess.stream().filter(s->!children.contains(s)).collect(Collectors.toSet());
        System.out.printf("上一从节点 : %s;\n当前从节点 : %s;\n移除从节点 : %s;\n\n",Arrays.toString(toPrecess.toArray()),Arrays.toString(children.toArray()),Arrays.toString(removeChildrens.toArray()));
        //匹配完成，将当前从节点存储到缓存中。
        this.toPrecess = children;
        return removeChildrens;
    }
}
