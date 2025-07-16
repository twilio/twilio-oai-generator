package com.twilio.oai.java.factory;

import org.openapitools.codegen.CodegenOperation;

/*
    * This interface defines a factory for creating processors that handle specific types of input.
    * It is generic and can be used to create processors for various types of input.
    * The type T represents the processor type, and E represents the input type.
    * Example implementations could include factories for creating processors for request bodies or response bodies.
 */
public interface ProcessorFactory<T, E> {
    T getProcessor(E input);
}