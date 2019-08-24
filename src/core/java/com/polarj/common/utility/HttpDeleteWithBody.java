package com.polarj.common.utility;

import java.net.URI;

import javax.annotation.concurrent.NotThreadSafe;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * 重写httpdelete方法，带上setEntity
 * Description: HttpDelete 使用 body 传递参数
 */
@NotThreadSafe
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public HttpDeleteWithBody() {
    }

    public HttpDeleteWithBody(URI uri) {
        this.setURI(uri);
    }

    public HttpDeleteWithBody(String uri) {
        this.setURI(URI.create(uri));
    }

    public String getMethod() {
        return "DELETE";
    }
}
