package com.vic.aspect;

import com.vic.annotation.CheckRepeatSubmit;
import com.vic.component.CacheComponent;
import com.vic.enums.CacheKey;
import com.vic.enums.ResultEnum;
import com.vic.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 校验重复提交切面
 * @author 罗利华
 * date: 2020/3/30 16:18
 */
@Slf4j
@Component
@Aspect
public class CheckRepeatSubmitAspect {

    private static final String TOKEN_KEY = "token";

    @Autowired
    private CacheComponent cacheComponent;

    @Around("execution(* com.vic.*.contrller.*(..)) && @annotation(checkRepeatSubmit)")
    public Object doAround(ProceedingJoinPoint pjp, CheckRepeatSubmit checkRepeatSubmit) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader(TOKEN_KEY);
            String ip = getClientIp(request).replaceAll(":", "");
            if(StringUtils.isBlank(token)) {
                token = request.getParameter(TOKEN_KEY);
            }

            // 重复提交key
            String key = CacheKey.LOAN_CREDIT_REPEAT_SUBMIT.getKey(token, ip, request.getServletPath());
            log.info("重复提交key:{}", key);
            // 添加
            Boolean add = cacheComponent.setNx(key, "1", checkRepeatSubmit.delaySeconds());
            if(!add) {
                return ResultVo.fail(ResultEnum.REPEAT_SUBMISSION_ERROR);
            }
            ResultVo result = (ResultVo) pjp.proceed();
            return result;
        } catch (Throwable e) {
            log.error("校验表单重复提交时异常: ", e);
            return ResultVo.fail();
        }
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (org.springframework.util.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (org.springframework.util.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
