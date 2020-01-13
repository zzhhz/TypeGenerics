package com.zzh.lib.type.type;

import com.zzh.lib.type.exception.TypeException;
import com.zzh.lib.type.type.impl.ParameterizedTypeImpl;
import com.zzh.lib.type.type.impl.WildcardTypeImpl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeGenericBuilder {
    private final TypeGenericBuilder parent;
    private final Class raw;
    private final List<Type> args = new ArrayList<>();


    private TypeGenericBuilder(Class raw, TypeGenericBuilder parent) {
        assert raw != null;
        this.raw = raw;
        this.parent = parent;
    }

    public static TypeGenericBuilder newInstance(Class raw) {
        return new TypeGenericBuilder(raw, null);
    }

    private static TypeGenericBuilder newInstance(Class raw, TypeGenericBuilder parent) {
        return new TypeGenericBuilder(raw, parent);
    }


    public TypeGenericBuilder beginSubType(Class raw) {
        return newInstance(raw, this);
    }

    public TypeGenericBuilder endSubType() {
        if (parent == null) {
            throw new TypeException("expect beginSubType() before endSubType()");
        }

        parent.addTypeParam(getType());

        return parent;
    }

    public TypeGenericBuilder addTypeParam(Class clazz) {
        return addTypeParam((Type) clazz);
    }

    public TypeGenericBuilder addTypeParamExtends(Class... classes) {
        if (classes == null) {
            throw new NullPointerException("addTypeParamExtends() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(null, classes);

        return addTypeParam(wildcardType);
    }

    public TypeGenericBuilder addTypeParamSuper(Class... classes) {
        if (classes == null) {
            throw new NullPointerException("addTypeParamSuper() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(classes, null);

        return addTypeParam(wildcardType);
    }

    public TypeGenericBuilder addTypeParam(Type type) {
        if (type == null) {
            throw new NullPointerException("addTypeParam expect not null Type");
        }

        args.add(type);

        return this;
    }

    public Type build() {
        if (parent != null) {
            throw new TypeException("expect endSubType() before build()");
        }

        return getType();
    }

    private Type getType() {
        if (args.isEmpty()) {
            return raw;
        }
        return new ParameterizedTypeImpl(raw, args.toArray(new Type[args.size()]), null);
    }
}
