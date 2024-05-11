package thesis.utils.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import thesis.utils.constant.URLConst;

import java.util.Arrays;

@Aspect
@Component
public class ControllerAspect {
    @Before("@annotation(thesis.utils.aop.StaticConstant)")
    public void injectStaticConstant(JoinPoint joinPoint) {
        Model model = (Model) Arrays.stream(joinPoint.getArgs())
                .filter(f -> f instanceof Model)
                .findFirst().orElse(null);
        if (model != null) {
            model.addAttribute("URLConst", URLConst.class);
        }
    }
}
