package com.vic.utils;


import com.vic.enums.ResultEnum;
import com.vic.vo.ResultVo;

public class ResultVOUtil {

    public static ResultVo success(Object object) {
        ResultVo resultVO = new ResultVo();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVo error(ResultEnum resultEnum) {
        ResultVo resultVO = new ResultVo();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMsg(resultEnum.getMessage());
        return resultVO;
    }

    public static ResultVo success() {
        ResultVo resultVO = new ResultVo();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        return resultVO;

    }
}
