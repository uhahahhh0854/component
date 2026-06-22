package com.hanamizuki.component.permission.handler;

import com.hanamizuki.component.permission.chain.ScrutinyDescriptor;

import java.util.*;
import java.util.stream.Collectors;

public class ScrutinyBus {

    private List<ScrutinyDescriptor> scrutinies = new ArrayList<>();

    public ScrutinyBus(){

    }

    public ScrutinyBus(List<ScrutinyDescriptor> scrutinies){
        this.scrutinies = scrutinies.stream()
                .sorted(Comparator.comparingInt(ScrutinyDescriptor::order)).collect(Collectors.toList());
    }

    public void doFilter(){
        for (ScrutinyDescriptor scrutinyDescriptor : scrutinies) {
            if (scrutinyDescriptor.support()) {
                boolean verify = scrutinyDescriptor.verify();
                if (!verify) {
                    throw new RuntimeException("Permission verification failed.");
                }
            }
        }
    }

}
