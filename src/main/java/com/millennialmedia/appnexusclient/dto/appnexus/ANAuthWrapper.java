package com.millennialmedia.appnexusclient.dto.appnexus;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: yiwengao
 * Date: 2/3/14
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ANAuthWrapper {
    @JsonProperty
    private final ANAuth auth;

    public ANAuthWrapper(ANAuth auth) {
        this.auth = auth;
    }

    public ANAuth getUserCredentials() {
        return auth;
    }

    @Override
    public String toString() {
        return StringUtils.join(new String[]{auth.getUsername(), auth.getPassword()});
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ANAuthWrapper)) return false;

        ANAuthWrapper auth1 = (ANAuthWrapper) o;

        if (auth != null ? !auth.equals(auth1.auth) : auth1.auth != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return auth != null ? auth.hashCode() : 0;
    }
}
