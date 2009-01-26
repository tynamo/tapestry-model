package org.trailsframework.conversations.services;

public interface RequestHandlerDecorator {
    public <T> T build(Class<T> serviceInterface, T delegate);

}
