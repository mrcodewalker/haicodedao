package com.example.codewalker.kma.exceptions;
import jakarta.persistence.criteria.CriteriaBuilder;

public class InvalidParamException extends Exception{
    public InvalidParamException(String message){
        super(message);
    }
}
