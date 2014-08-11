package com.qmunity.lib.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MixinFactory {
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static class MixinInvocationHandler implements InvocationHandler {
        
        private Map implMap     = new HashMap();
        private Map methodCache = new HashMap();
        
        public MixinInvocationHandler(Object core, Class... classes) {
        
            for (Class<?> clazz : classes) {
                for (int i = 0; i < clazz.getInterfaces().length; i++) {
                    Class anInterface = clazz.getInterfaces()[i];
                    try {
                        Class delegateClass = Class.forName(anInterface.getName() + "Impl");
                        Constructor delegateConstructor = findBestMatchConstrustor(delegateClass, core.getClass());
                        if (delegateConstructor != null) {
                            implMap.put(anInterface, delegateConstructor.newInstance(new Object[] { core }));
                        } else {
                            implMap.put(anInterface, core);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
            Object delegate = implMap.get(method.getDeclaringClass());
            
            return getImplMethod(delegate.getClass(), method).invoke(delegate, args);
        }
        
        private Method getImplMethod(Class implClass, Method method) throws NoSuchMethodException {
        
            if (methodCache.containsKey(method)) {
                return (Method) methodCache.get(method);
            } else {
                Method declaredMethod = implClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                methodCache.put(method, declaredMethod);
                return declaredMethod;
            }
        }
        
        private static Constructor findBestMatchConstrustor(Class delegateClass, Class coreClass) {
        
            if (coreClass == null) { return null; }
            try {
                return delegateClass.getConstructor(new Class[] { coreClass });
            } catch (NoSuchMethodException e) {
                Class[] interfaces = coreClass.getInterfaces();
                for (int i = 0; interfaces != null && i < interfaces.length; i++) {
                    Class interfaceClass = interfaces[i];
                    Constructor delegateConstructor = findBestMatchConstrustor(delegateClass, interfaceClass);
                    if (delegateConstructor != null) { return delegateConstructor; }
                }
                Constructor delegateConstructor = findBestMatchConstrustor(delegateClass, coreClass.getSuperclass());
                if (delegateConstructor != null) { return delegateConstructor; }
            }
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T create(Object base, Class<?>[] interfaces) {
    
        try {
            return (T) Proxy.getProxyClass(base.getClass().getClassLoader(), interfaces).getConstructor(new Class[] { InvocationHandler.class })
                    .newInstance(new Object[] { new MixinInvocationHandler(base, interfaces) });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
