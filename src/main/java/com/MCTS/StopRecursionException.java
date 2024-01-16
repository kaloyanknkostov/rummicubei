package com.MCTS;

public class StopRecursionException extends RuntimeException {
    public StopRecursionException() {
        super("Stopping recursion exception");
    }
}
