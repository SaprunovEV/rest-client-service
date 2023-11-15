package by.sapra.restclientservice.scoupes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

@Scope(value = SCOPE_REQUEST, proxyMode = TARGET_CLASS)
@Component
public class RequestIdHolder extends AbstractIdHolder {
    @Override
    String holderType() {
        return "Request";
    }
}
