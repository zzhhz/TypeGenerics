package com.zzh.lib.type.type;

import com.zzh.lib.type.exception.TypeException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeGenericToken<T> {
    private final Type type;

    public TypeGenericToken() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new TypeException("No generics found!");
        }
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}
