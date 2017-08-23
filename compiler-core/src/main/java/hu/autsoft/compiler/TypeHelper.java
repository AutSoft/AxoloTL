package hu.autsoft.compiler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hu.autsoft.compiler.exception.InvalidNameException;
import hu.autsoft.compiler.exception.InvalidTypeArgumentException;
import hu.autsoft.compiler.exception.NoClassNameException;

public class TypeHelper {

    private static class TypeHelperCacheLoader extends CacheLoader<String, TypeHelper> {
        Map<String, TypeHelper> defaultElements = new HashMap<>();

        TypeHelperCacheLoader() {
            defaultElements.put("void", new TypeHelper("void", TypeName.VOID));
            defaultElements.put("byte", new TypeHelper("byte", TypeName.BYTE));
            defaultElements.put("short", new TypeHelper("short", TypeName.SHORT));
            defaultElements.put("int", new TypeHelper("int", TypeName.INT));
            defaultElements.put("long", new TypeHelper("long", TypeName.LONG));
            defaultElements.put("float", new TypeHelper("float", TypeName.FLOAT));
            defaultElements.put("double", new TypeHelper("double", TypeName.DOUBLE));
            defaultElements.put("char", new TypeHelper("char", TypeName.CHAR));
            defaultElements.put("boolean", new TypeHelper("boolean", TypeName.BOOLEAN));
        }

        @Override
        public TypeHelper load(String fullName) throws Exception {
            if (defaultElements.containsKey(fullName)) {
                return defaultElements.get(fullName);
            }
            return new TypeHelper(fullName);
        }
    }

    private static LoadingCache<String, TypeHelper> typeHelperMap = CacheBuilder.newBuilder()
            .maximumSize(200)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new TypeHelperCacheLoader());

    public static TypeHelper getTypeHelper(final String fullName) {
        return typeHelperMap.getUnchecked(fullName);
    }

    public static TypeHelper getVoid() {
        return getTypeHelper("void");
    }

    public static TypeHelper getObject() {
        return getTypeHelper(Object.class);
    }

    public static TypeHelper getTypeHelper(Class c) {
        return getTypeHelper(c.getName());
    }

    private static TypeName getTypeNameFromString(String name) {
        String filteredName = name.replace(" ", "");
        try {
            if (filteredName.contains("<")) {
                return getParameterizedTypeName(filteredName);
            } else {
                return getTypeNameOrArrayTypeName(filteredName);
            }
        } catch (Exception ex) {
            throw new InvalidNameException(ex);
        }
    }

    private static TypeName getParameterizedTypeName(String name) {
        String baseName = name.substring(0, name.indexOf('<'));
        String genericParamsStr = name.substring(name.indexOf('<') + 1, name.lastIndexOf('>'));
        char[] chars = genericParamsStr.toCharArray();
        int innerCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '<') {
                innerCount++;
            } else if (chars[i] == '>') {
                innerCount--;
            } else if (chars[i] == ',' && innerCount == 0) {
                chars[i] = '!';
            }
        }
        genericParamsStr = new String(chars);
        String[] paramArray = genericParamsStr.split("!");
        TypeName[] typeNames = new TypeName[paramArray.length];
        for (int i = 0; i < paramArray.length; i++) {
            typeNames[i] = getTypeNameFromString(paramArray[i]);
        }
        return ParameterizedTypeName.get(ClassName.bestGuess(baseName), typeNames);
    }

    private static TypeName getTypeNameOrArrayTypeName(String name) {
        if (name.endsWith("[]")) {
            String rawName = name.substring(0, name.length() - 2);
            TypeHelper rawTypeHelper = TypeHelper.getTypeHelper(rawName);
            return ArrayTypeName.of(rawTypeHelper.getTypeName());
        } else {
            return ClassName.bestGuess(name);
        }
    }

    private String name;
    private TypeName typeName;
    private ParameterizedTypeName parameterizedTypeName;
    private ClassName className;

    private String strPackageName;
    private String strClassName;
    private String strFullClassName;

    private TypeHelper(String name) {
        this(name, TypeHelper.getTypeNameFromString(name));
    }

    private TypeHelper(String name, TypeName typeName) {
        this.name = name;
        this.typeName = typeName;
        if (typeName instanceof ParameterizedTypeName) {
            parameterizedTypeName = (ParameterizedTypeName) typeName;
            className = parameterizedTypeName.rawType;
        } else if (typeName instanceof ClassName) {
            className = (ClassName) typeName;
        }
        if (className != null) {
            initNames(className.packageName(), className.simpleName());
        }
    }

    private void initNames(String strPackageName, String strClassName) {
        this.strPackageName = strPackageName;
        this.strClassName = strClassName;
        this.strFullClassName = strPackageName + "." + strClassName;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public TypeName getTypeNameBoxed() {
        return typeName.box();
    }

    public boolean isVoid() {
        return typeName.equals(TypeName.VOID);
    }

    public boolean hasClassName() {
        return className != null;
    }

    public ClassName getClassName() {
        if (!hasClassName()) {
            throw new NoClassNameException(strFullClassName, name);
        }
        return className;
    }

    public String getStrFullClassName() {
        return strFullClassName;
    }

    public String getStrClassName() {
        return strClassName;
    }

    public String getStrPackageName() {
        return strPackageName;
    }

    public boolean isParameterized() {
        return parameterizedTypeName != null;
    }

    public TypeHelper getParamTypeArgument() {
        if (parameterizedTypeName.typeArguments == null || parameterizedTypeName.typeArguments.size() != 1) {
            throw new InvalidTypeArgumentException(name);
        }
        return TypeHelper.getTypeHelper(parameterizedTypeName.typeArguments.get(0).toString());
    }

    public boolean isClass(Class c) {
        if (className == null) {
            return false;
        }
        return c.getName().equals(className.packageName() + "." + className.simpleName());
    }
}