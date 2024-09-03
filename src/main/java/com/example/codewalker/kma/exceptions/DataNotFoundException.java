package com.example.codewalker.kma.exceptions;
import org.hibernate.sql.ast.tree.expression.Over;

public class DataNotFoundException extends Exception{
    public DataNotFoundException(String message){
        super(message);
    }
}
