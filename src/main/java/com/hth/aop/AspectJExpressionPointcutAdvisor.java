package com.hth.aop;

import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor{
    /**
     *  AspectJ表达式切点匹配器
     *  AspectJ表达式匹配的切点，默认有初始化。也默认有了MethodMatcher(AspectJExpressionPointcut实现了MethodMatcher接口)
     */
    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    /**
     * 方法拦截器
     * 	  这个要用户自己去xml文件里配置方法拦截器(MethodInterceptor继承了Advice接口)，
     *   在AspectJAwareAdvisorAutoProxyCreator设置代理对象的方法拦截器时将Advispr强转为MethodInterceptor
     */
    private Advice advice;
    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setExpression(String expression) {
        this.pointcut.setExpression(expression);
    }
}
