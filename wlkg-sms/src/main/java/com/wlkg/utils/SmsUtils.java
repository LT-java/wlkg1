package com.wlkg.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wlkg.config.SmsProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.rmi.ServerException;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtils {
    @Autowired
    private  SmsProperties properties;


    //产品域名，无序替换
    static final String domain = "dysmsapi.aliyuncs.com";

    static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);


    public CommonResponse sendSms(String phone,String code) {
        //初始化acsClient,暂不支持region化
        DefaultProfile profile = DefaultProfile.getProfile("default", properties.getAccessKeyId(), properties.getAccessKeySecret());

        IAcsClient client = new DefaultAcsClient(profile);


        //组装请求对象，具体描述见控制台 文档内容
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");


        //必填待发送手机号
        request.putQueryParameter("PhoneNumbers", phone);

        //必填短信签名
        request.putQueryParameter("SignName", properties.getSignName());

        //必填短信模板
        request.putQueryParameter("TemplateCode", properties.getVerifyCodeTemplate());

        //发送内容
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");



            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println("发送短信结果为：" + response.getData());
                return response;
            } catch (ClientException e) {
                e.printStackTrace();
            }
            return null;
    }

}
