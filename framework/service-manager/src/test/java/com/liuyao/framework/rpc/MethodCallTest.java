package com.liuyao.framework.rpc;

import com.liuyao.framework.lang.ExecutionException;
import com.liuyao.framework.service.api.Animinal;
import com.liuyao.framework.service.api.Dog;
import com.liuyao.framework.service.meta.ServiceInfMeta;
import com.liuyao.framework.service.reg.ServiceRegister;
import com.liuyao.framework.service.reg.ServiceRegisterImpl;
import org.junit.Test;

import java.util.Optional;

public class MethodCallTest {


    @Test
    public void testMethod() throws ExecutionException, ClassNotFoundException {
        Dog dog = new Dog();

        ServiceRegister serviceRegister = new ServiceRegisterImpl();
        serviceRegister.registerService(Animinal.class, dog);

        Object[] args = new Object[]{12};

        System.out.println(serviceRegister.isServicePresent(Class.forName(Animinal.class.getName())));

        Optional re = serviceRegister.getServiceMeta(Animinal.class).invoke("earn", args);
        if (re.isPresent()) {
            System.out.println(re.get());
        }

        Object[] params = new Object[]{"hello Huanhuan"};
        Optional result = serviceRegister.getServiceMeta(Animinal.class).invoke("fee", params);
        System.out.println(result.isPresent());
    }
}
