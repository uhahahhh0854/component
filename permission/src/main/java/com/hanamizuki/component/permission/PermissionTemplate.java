package com.hanamizuki.component.permission;

import com.hanamizuki.component.permission.annotation.Permission;
import com.hanamizuki.component.permission.handler.ScrutinyBus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionTemplate {

    public PermissionTemplate(ScrutinyBus scrutinyBus) {
        this.scrutinyBus = scrutinyBus;
    }

    private ScrutinyBus scrutinyBus;

    @Pointcut("@annotation(permission)")
    public void pointCut(Permission permission) {}

    @Around(value = "pointCut(permission)", argNames = "joinPoint,permission")
    public Object around(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        scrutinyBus.doFilter();


        return joinPoint.proceed();
    }


    public ScrutinyBus getScrutinyBus() {
        return scrutinyBus;
    }

    public void setScrutinyBus(ScrutinyBus scrutinyBus) {
        this.scrutinyBus = scrutinyBus;
    }
}
