package by.sapra.restclientservice.scoupes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Scope(value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
@Component
public class SessionIdHolder extends AbstractIdHolder {
    @Override
    String holderType() {
        return "Session";
    }
}
