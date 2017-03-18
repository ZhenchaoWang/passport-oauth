package org.zhenchao.passport.oauth.domain;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.zhenchao.passport.oauth.common.ErrorCode;

/**
 * 返回信息
 *
 * @author zhenchao.wang 2017-01-23 13:52
 * @version 1.0.0
 */
public class ResultInfo {

    private int code;

    private String desc;

    private String uri;

    private String state;

    public ResultInfo() {
    }

    public ResultInfo(String desc) {
        this.code = 200;
        this.desc = desc;
        this.uri = StringUtils.EMPTY;
        this.state = StringUtils.EMPTY;
    }

    public ResultInfo(String desc, String state) {
        this.code = 200;
        this.desc = desc;
        this.uri = StringUtils.EMPTY;
        this.state = state;
    }

    public ResultInfo(int code, String desc, String state) {
        this.code = code;
        this.desc = desc;
        this.uri = StringUtils.EMPTY;
        this.state = StringUtils.trimToEmpty(state);
    }

    public ResultInfo(int code, String desc, String uri, String state) {
        this.code = code;
        this.desc = desc;
        this.uri = uri;
        this.state = StringUtils.trimToEmpty(state);
    }

    public ResultInfo(ErrorCode errorCode, String state) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
        this.uri = StringUtils.EMPTY;
        this.state = StringUtils.trimToEmpty(state);
    }

    public ResultInfo(ErrorCode errorCode, String uri, String state) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
        this.uri = uri;
        this.state = StringUtils.trimToEmpty(state);
    }

    /**
     * 转化成json
     *
     * @return
     */
    public JSONObject toJson() {
        JsonConfig config = new JsonConfig();
        config.setJsonPropertyFilter((Object source, String name, Object value) -> {
            if ("uri".equals(name) || "state".equals(name)) {
                if (StringUtils.isBlank((String) value)) {
                    return true;
                }
            }
            return false;
        });
        return JSONObject.fromObject(this, config);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
