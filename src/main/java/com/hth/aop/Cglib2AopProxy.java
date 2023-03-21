package com.hth.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *  cglib是针对类来实现代理的，他的原理是对指定的目标类生成一个子类，并覆盖其中方法实现增强，但
 *  因为采用的是继承， 所以不能对final修饰的类进行代理。
 *
 */
public class Cglib2AopProxy extends AbstractAopProxy{

    public Cglib2AopProxy(AdvisedSupport advised) {
        super(advised);
    }
    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTargetClass());
        enhancer.setInterfaces(advised.getTargetSource().getInterfaces());
        // 设置回调方法
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        Object enhanced = enhancer.create();
        return enhanced;
    }

    /** 注意该类实现的是net.sf.cglib.proxy.MethodInterceptor，不是aopalliance的MethodInterceptor.
     */
    private static class DynamicAdvisedInterceptor implements MethodInterceptor{
        private AdvisedSupport advised;

        // 用户写的的方法拦截器
        private org.aopalliance.intercept.MethodInterceptor delegateMethodInterceptor;
        private DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
            this.delegateMethodInterceptor = advised.getMethodInterceptor();
        }

        // 拦截代理对象的所有方法
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (advised.getMethodMatcher() == null
                    || advised.getMethodMatcher().matches(method, advised.getTargetSource().getTargetClass())){
                // delegateMethodInterceptor通过advised.getMethodInterceptor()得到用户写的方法拦截器
                // 返回去调用用户写的拦截器的invoke方法(用户根据需要在调用proceed方法前后添加相应行为,例如：TimerInterceptor.java)
                return delegateMethodInterceptor.invoke(new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, args, proxy));
            }
            // 有AspectJ表达式，但没有匹配该方法，返回通过methodProxy调用原始对象的该方法
            return new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, args, proxy).proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation{
        // 方法代理
        private final MethodProxy methodProxy;
        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);

            this.methodProxy = methodProxy;
        }
        // 通过methodProxy调用原始对象的方法
        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(this.target, this.arguments);
        }
    }


}
