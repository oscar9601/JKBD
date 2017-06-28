package cn.ucai.jkbd.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class aaaa {
    /**
     * error_code : 0
     * reason : ok
     * result : ["asd"]
     */

    private int error_code;
    private String reason;
    private List<String> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
