package com.hth.aop;

import com.hth.beans.BeanPostProcessor;
import com.hth.beans.factory.AbstractBeanFactory;
import com.hth.beans.factory.BeanFactory;

/**
 * 实现了BeanFactoryAware接口：这个接口提供了对 BeanFactory 的感知，这样，尽管它是容器中的一个 Bean，却
 * 可以获取容器 的引用，进而获取容器中所有的切点对象，决定对哪些对象的哪些方法进行代理。解决了 为哪些对象
 * 提供 AOP 的植入 的问题。
 *
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware{
    private AbstractBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return null;
    }
}
