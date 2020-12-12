package com.zhaoweihao.architechturesample.interfaze;

import com.zhaoweihao.architechturesample.bean.ValidationMesg;

import java.util.List;

/**
*@description 用于检测验证消息
*@author
*@time 2019/3/8 14:58
*/
public interface ValidationMesgSerrvice {
    List<ValidationMesg> detectedNew();
}
