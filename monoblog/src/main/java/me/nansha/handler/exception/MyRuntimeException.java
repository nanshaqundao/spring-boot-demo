package me.nansha.handler.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRuntimeException extends RuntimeException {
    private String code;
    private String message;
}
