package com.example.fluxdataconsumerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MyObj(
        @JsonProperty("id") long id,
        @JsonProperty("content") String content
) {}