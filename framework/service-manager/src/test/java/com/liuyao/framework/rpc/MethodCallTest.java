package com.liuyao.framework.rpc;

import com.liuyao.framework.lang.ExecutionException;
import com.liuyao.framework.service.api.Amininal;
import com.liuyao.framework.service.api.Dog;
import com.liuyao.framework.service.meta.ServiceInfMeta;
import org.junit.Test;

import java.util.Optional;

public class MethodCallTest {


    @Test
    public void testMethod() throws ExecutionException {
        Dog dog = new Dog();
        ServiceInfMeta serviceInfMeta = ServiceInfMeta.builder().setService(Amininal.class, new Dog()).builder();

        Object[] args = new Object[]{12};

        Optional re = serviceInfMeta.invoke("earn", args);
        if (re.isPresent()) {
            System.out.println(re.get());
        }
    }
}
