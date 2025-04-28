package org.example.ejb_web.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Đăng ký các REST controllers
        classes.add(StaffRestController.class);
        classes.add(AdminRestController.class);
        classes.add(CategoryRestController.class);
        classes.add(ProductRestController.class);
        classes.add(OrderRestController.class);
        classes.add(OrdersProductRestController.class);
        return classes;
    }
}