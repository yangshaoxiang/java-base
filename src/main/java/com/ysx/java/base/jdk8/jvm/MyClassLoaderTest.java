package com.ysx.java.base.jdk8.jvm;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * 自定义类加载器只需要继承 java.lang.ClassLoader 类，该类有两个核心方法，一个是loadClass(String, boolean)，实现了双亲委派机制，
 * 还有一个方法是findClass，默认实现是空方法，所以我们自定义类加载器主要是重写findClass方法。
 */
public class MyClassLoaderTest {
    static class MyClassLoader extends ClassLoader {
        private String classPath;


        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }


        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }


        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                //defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }


        /**
         * 重写类加载方法，实现自己的加载逻辑，不委派给双亲加载 === 打破双亲委派
         * @param name
         * @param resolve
         * @return
         * @throws ClassNotFoundException
         */
       // @Override
        protected Class<?> loadClass1(String name, boolean resolve)
                throws ClassNotFoundException {
            if(!name.contains("com.ysx.java.base.jdk8.jvm")){
                return super.loadClass(name,resolve);
            }
            synchronized (getClassLoadingLock(name)) {
                // First, check if the class has already been loaded
                Class<?> c = findLoadedClass(name);

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }
    }






    public static void main(String args[]) throws Exception {
        //初始化自定义类加载器，会先初始化父类 ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器 AppClassLoader
        MyClassLoader classLoader = new MyClassLoader("D:/test");
        //D盘创建 test/com/ysx/java.... 几级目录，将ClassLoadTestEntry类的复制类ClassLoadTestEntry.class丢入该目录,修改项目下的 ClassLoadTestEntry 为 ClassLoadTestEntry1
        Class clazz = classLoader.loadClass("com.ysx.java.base.jdk8.jvm.ClassLoadTestEntry");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("test", null);
        method.invoke(obj, null);
        System.out.println(clazz.getClassLoader().getClass().getName());
    }

}