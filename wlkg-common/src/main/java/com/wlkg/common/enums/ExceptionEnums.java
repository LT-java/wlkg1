package com.wlkg.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    //40~：
    INVALID_VERFIY_CODE(1000,"验证码不正确"),
    NO_AUTHORIZED(1001,"未授权的用户"),
    USERNAME_PASSWORD_NOT_FOUND(1001,"账号或密码错误"),
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    GOODS_NOT_FOUNT(2000,"商品查询无结果"),
    SPUDETAIL_NOT_FOUND(3000,"商品详情无结果"),
    SPUDETAIL_UPDATE_ERROR(3000,"商品详情更新失败"),
    SPU_UPDATE_ERROR(2000,"商品更新失败"),
    SKUS_NOT_FOUND(5000,"商品个体查找无结果");

    private int code;
    private String msg;

}
