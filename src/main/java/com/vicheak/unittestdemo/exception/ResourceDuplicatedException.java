package com.vicheak.unittestdemo.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDuplicatedException extends RuntimeException {
    private String message;
}
