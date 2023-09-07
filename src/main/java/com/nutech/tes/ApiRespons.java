package com.nutech.tes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ApiRespons<T> {
    private  Integer status;
    private  String message;
    private T data;

}
