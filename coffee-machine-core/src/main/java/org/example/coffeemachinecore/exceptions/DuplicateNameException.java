package org.example.coffeemachinecore.exceptions;

/**
 * Исключение для случая, когда уже существует элемент с таким именем
 */
public class DuplicateNameException extends RuntimeException{
    public DuplicateNameException(String message){
        super(message);
    }
}
