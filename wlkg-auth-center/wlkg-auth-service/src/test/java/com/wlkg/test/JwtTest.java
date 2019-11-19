package com.wlkg.test;

import com.wlkg.WlkgAuthService;
import com.wlkg.entity.UserInfo;
import com.wlkg.utils.JwtUtils;
import com.wlkg.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WlkgAuthService.class)
public class JwtTest {

    private static final String pubKeyPath = "F:\\File\\rsa.pub";

    private static final String priKeyPath = "F:\\File\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception{
        RsaUtils.generateKey(pubKeyPath,priKeyPath,"1997");
    }

    @Before
    public void testGetRsa() throws Exception{
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception{
        //生成Token
        String token = JwtUtils.generateToken(new UserInfo(20L,"LT"),privateKey,5);

        UserInfo user = JwtUtils.getInfoFromToken(token,publicKey);

        System.out.println("id:" + user.getId());
        System.out.println("username:" + user.getUsername());
        System.out.println("token = "+ token);
    }

}
