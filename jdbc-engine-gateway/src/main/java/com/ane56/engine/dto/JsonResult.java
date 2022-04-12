package com.ane56.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Data
public class JsonResult<T> {
    private T data;
    private int code;
    private String msg;

    public JsonResult() {
        setCode(0);
        setMsg("success");
    }

    public JsonResult(T data) {
        setCode(0);
        setMsg("success");
        setData(data);
    }

    public JsonResult(int code, String msg) {
        setCode(code);
        setMsg(msg);
    }

    public JsonResult(String msg, T data) {
        setMsg(msg);
        setCode(0);
        setData(data);
    }
}
