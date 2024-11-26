package com.py.core.kit;

import com.py.core.BaseHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
public class ClassKit {


    /**
     * 递归扫描目录下的所有类文件
     *
     * @param dir         目录
     * @param packageName 包名
     * @param classes     类集合
     */
    private static void scanDirectory(File dir, String packageName, Set<Class<?>> classes) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Class not found: " + className, e);
                    }
                }
            }
        }
    }

    /**
     * 扫描指定包及其子包下的所有类
     *
     * @param packageName 包名
     * @return 包含所有类的集合
     */
    public static Set<Class<?>> scanPackage(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File dir = new File(resource.getFile());
                if (dir.exists()) {
                    scanDirectory(dir, packageName, classes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan classpath for package " + packageName, e);
        }
        return classes;
    }

    // 辅助方法：获取参数类型的数组
    public static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return parameterTypes;
    }


    public static void main(String[] args) {
        Set<Class<?>> classes = scanPackage("com.py.eventBus");
        for (Class<?> clazz : classes) {
            if (BaseHandler.class.isAssignableFrom(clazz) && clazz != BaseHandler.class) {
                System.out.println(clazz.getName());
            }
        }
    }

}
