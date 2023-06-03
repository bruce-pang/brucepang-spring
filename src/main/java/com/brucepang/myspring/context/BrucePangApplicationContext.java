package com.brucepang.myspring.context;

import com.brucepang.myspring.annotation.Component;
import com.brucepang.myspring.annotation.ComponentScan;
import com.brucepang.myspring.annotation.Scope;
import com.brucepang.myspring.factory.config.BeanDefinition;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author BrucePang
 */
public class BrucePangApplicationContext {

    private Class configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private Map<String,Object> singletonObjects = new ConcurrentHashMap<>();
    public BrucePangApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描
        scan(configClass);

        // 创建单例Bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();

            if ("singleton".equals(beanDefinition.getScope())){
               Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }

    }

    private Object createBean(String beanName,BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType(); // 获取Class对象
        try {
            Object instance = clazz.getConstructor().newInstance();// 通过反射无参构造器创建对象
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return  null;
    }

    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) { // 判断传过来的类是否有ComponentScan注解
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);// 获取ComponentScan注解
            String scanPath = componentScanAnnotation.value();// 获取ComponentScan注解的value值(扫描路径的地址)
            scanPath = scanPath.replace(".", "/");// 将.替换为/

            ClassLoader classLoader = BrucePangApplicationContext.class.getClassLoader(); // 获取类加载器
            URL resource = classLoader.getResource(scanPath);// 获取类路径的地址
            File file = new File(resource.getFile());// 将类路径的地址转换为文件(目录)
            if (file.isDirectory()) {// 判断是否是目录
                File[] files = file.listFiles();// 获取目录下的所有文件
                for (File f : files) {
                    String absolutePath = f.getAbsolutePath();// 获取文件的绝对路径

                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));// 截取类的全限定名(包名+类名)
                    absolutePath = absolutePath.replace("\\", ".");// 将\替换为.【windows是这种写法】

                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath);// 加载类(将类加载到内存中)
                        if (clazz.isAnnotationPresent(Component.class)) { // 判断类是否有Component注解
                            // 创建BeanDefinition对象，保存Bean的相关信息
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz); // 保存加载到内存的Class对象信息

                            // 判断Component注解内容[别名]，为null就以类名首字母小写作为beanName
                            String beanName = clazz.getAnnotation(Component.class).value(); // 获取类的别名
                            if (beanName == null || "".equals(beanName)) { // 判断别名是否为空
                                beanName = clazz.getSimpleName();// 获取类名
                                beanName = Introspector.decapitalize(beanName);// 将类名首字母小写

                            }

                            // 判断Scope注解内容
                            if (clazz.isAnnotationPresent(Scope.class)){ // 判断类是否有Scope注解
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String value = scopeAnnotation.value();
                                beanDefinition.setScope(value);
                            } else {
                                beanDefinition.setScope("singleton");
                            }

                            //
                            beanDefinitionMap.put(beanName, beanDefinition);// 将beanName和beanDefinition对象保存到map中

                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }


                }
            }

        }
    }


    public Object getBean(String beanName) {
        // beanName --> ***.class -->
        // 1.获取BeanDefinition对象
        if (!beanDefinitionMap.containsKey(beanName)) { // 判断beanName是否存在
            throw new NullPointerException();
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefinition.getScope())) { // 判断作用域是否是单例
            Object singletonBean = singletonObjects.get(beanName);
            return singletonBean;
        } else if ("prototype".equals(beanDefinition.getScope())) { // 判断作用域是否是多例
            Object prototypeBean = createBean(beanName,beanDefinition);
            return prototypeBean;
        } else {
            throw new RuntimeException("暂不支持的作用域");
        }

    }
}
