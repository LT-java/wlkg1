package com.wlkg.service;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.utils.CodecUtils;
import com.wlkg.common.utils.NumberUtils;
import com.wlkg.mapper.UserMapper;
import com.wlkg.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;


    private final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //检验数据
    public Boolean checkData(String data, Integer type) {

        User user = new User();

        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;

        }

        return userMapper.selectCount(user) == 0;
    }

    //发送短信
    public Boolean sendVerifyCode(String phone){
        //生成验证码
        String code = NumberUtils.generateCode(6);
        try {
            //发送短信
            Map<String,String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            amqpTemplate.convertAndSend("wlkg.sms.exchange",
                    "sms.verify.code",msg);

            //将code存入到redis
            redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);
            return true;

        }catch(Exception e){
            logger.error("发送短信失败。phone:{},code:{}",phone,code);
            return false;
        }

    }

    //注册用户
    public void register(User user, String code) {
        //验证码的键
        String key = KEY_PREFIX + user.getPhone();

        //从redis中取出验证码
        String codeCache = redisTemplate.opsForValue().get(key);

        //检查验证码是否正确
        if(!code.equals(codeCache)){
            //不正确，返回
            throw new WlkgException(ExceptionEnums.INVALID_VERFIY_CODE);
        }

        user.setCreated(new Date());

        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);


        //对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //写入数据库
        boolean boo = userMapper.insertSelective(user) == 1;

        if(boo) {
            try {
                //如果注册成功就删除redis中的code
                redisTemplate.delete(key);
            }catch(Exception e){
                logger.error("删除缓存验证码失败，code:{}",code,e);
            }
        }

    }


    //根据用户名密码查询用户信息
    public User query(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User retuesr = userMapper.selectOne(user);

        if(retuesr!=null){
            String pwd = CodecUtils.md5Hex(password,retuesr.getSalt());
            if(pwd.equals(retuesr.getPassword())){
                return retuesr;
            }else{
                throw new WlkgException(ExceptionEnums.USERNAME_PASSWORD_NOT_FOUND);
            }
        }else{
            throw new WlkgException(ExceptionEnums.USERNAME_PASSWORD_NOT_FOUND);
        }
    }
}
