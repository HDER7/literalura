package com.example.literalura.service;

public interface IConverter {
    <T> T getData(String json, Class<T> clase);
}
