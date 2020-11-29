package com.vmware.numgenerator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Status {

    IN_PROGRESS("IN_PROGRESS"),

    SUCCESS("SUCCESS"),

    ERROR("ERROR");

    @Getter
    private String value;
}
