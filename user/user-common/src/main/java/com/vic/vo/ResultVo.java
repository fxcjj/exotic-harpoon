package com.vic.vo;

import com.vic.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVo<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public static ResultVo success() {
        ResultVo vo = new ResultVo();
        vo.setCode(ResultEnum.SUCCESS.getCode());
        vo.setMsg(ResultEnum.SUCCESS.getMessage());
        return vo;
    }

    public static ResultVo fail() {
        ResultVo vo = new ResultVo();
        vo.setCode(ResultEnum.FAIL.getCode());
        vo.setMsg(ResultEnum.FAIL.getMessage());
        return vo;
    }

    public static ResultVo fail(ResultEnum re) {
        ResultVo vo = new ResultVo();
        vo.setCode(re.getCode());
        vo.setMsg(re.getMessage());
        return vo;
    }

    public static <T> ResultVo<T> success(T data) {
        ResultVo vo = ResultVo.success();
        vo.setData(data);
        return vo;
    }

}
