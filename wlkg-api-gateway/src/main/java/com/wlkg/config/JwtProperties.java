package com.wlkg.config;

import com.wlkg.utils.RsaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "wlkg.jwt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtProperties {


    private String pubKeyPath; //公钥

    private PublicKey publicKey;

    private String cookieName;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        try {
            //获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);

        }catch(Exception e){
            logger.error("初始化公钥失败",e);
            throw new RuntimeException();
        }

    }
}
