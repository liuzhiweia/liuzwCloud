package com.liuzw.springcloud.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: liuzw
 * @date: 2021/9/3 7:40 下午
 * @e-mail: liuzhiwei1@ylzinfo.com
 * -------------------------------
 * @description： 统一返回前端封装
 * code:200：正常
 * 500：错误，错误信息在msg中
 * 502：用户鉴权错误
 * 555：异常抛出，异常信息在msg中
 */
@Data
public class R implements Serializable {

    private static final long serialVersionUID = 324875346923L;

    /**
     * 定义R对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

    public R() {
    }

    public R(Integer status, String msg, Object data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    public R(Object data) {
        this.code = 200;
        this.msg = "操作成功";
        this.data = data;
    }

    public static R build(Integer code, String msg, Object data) {
        return new R(code, msg, data);
    }

    public static R ok(String msg, Object data) {
        return new R(200, msg, data);
    }

    public static R ok(Object data) {
        return new R(200, "操作成功", data);
    }

    public static R ok(String msg) {
        return new R(200, msg, null);
    }

    public static R ok() {
        return new R(200, "操作成功", null);
    }

    public static R errorException(String msg, Object data) {
        return new R(555, msg, data);
    }

    public static R errorException(Object data) {
        return new R(555, "操作失败", data);
    }

    public static R errorException(String msg) {
        return new R(555, msg, null);
    }

    public static R errorAuth(String msg, Object data) {
        return new R(502, msg, data);
    }

    public static R errorAuth(Object data) {
        return new R(502, "操作失败", data);
    }

    public static R errorAuth(String msg) {
        return new R(502, msg, null);
    }

    public static R error(String msg) {
        return new R(500, msg, null);
    }

    public static R error(String msg, Object data) {
        return new R(500, msg, data);
    }

    public static R error(Object data) {
        return new R(500, "操作失败", data);
    }

    public static R error() {
        return new R(500, "操作失败", null);
    }

    public Boolean isOk() {
        return this.code == 200;
    }

    /**
     * 将json结果集转化为R对象内的data集合
     *
     * @param json  转换的结果集
     * @param clazz 类
     */
    public static R formatToPojo(String json, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(json, R.class);
            }
            JsonNode jsonNode = MAPPER.readTree(json);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null){
                if (data.isObject()){
                    obj =MAPPER.readValue(data.traverse(),clazz);
                }else if(data.isTextual()){
                    obj = MAPPER.readValue(data.asText(),clazz);
                }
            }
            return build(jsonNode.get("code").intValue(),jsonNode.get("msg").asText(), obj);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * @Description: 没有object对象的转化
     * @param json
     * @return
     */
    public static R format(String json) {
        try {
            return MAPPER.readValue(json, R.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: Object是集合转化
     *               需要转换的对象是一个list
     * @param jsonData
     * @param clazz
     * @return
     */
    public static R formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
