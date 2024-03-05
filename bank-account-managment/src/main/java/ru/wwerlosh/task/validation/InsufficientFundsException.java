package ru.wwerlosh.task.validation;

public class InsufficientFundsException extends TransferException {

    public InsufficientFundsException(String s) {
        super(s);
    }
}
