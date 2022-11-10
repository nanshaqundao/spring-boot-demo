# Configurations

### logging
* command 
```
  $ keytool -keystore mykeys.jks -genkey -alias tomcat -keyalg RSA
```
```yaml
logging:
  file:
    path: target
    file: springboot-local.log
  level:
    root: WARN
    org.springframework.security: DEBUG
```

### ssl
```yaml
server:
  port: 8443
  ssl:
    key-store: file:D:\github\nanshaqundao\spring-boot-demo\springinaction\ch05-basic\src\main\resources\mykeys.jks
    key-store-password: password
    key-password: password
```

### use property variable
