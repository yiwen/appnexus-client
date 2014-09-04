package com.millennialmedia.appnexusclient.dto.appnexus;

import java.util.Date;

/**
 * A wrapper object that wraps api access information such as appnexus token, request date, etc.
 * 
 * @author gyang
 * @since 1.0
 */
public class AccessToken
{
    private final String appnexusToken;
    private final Date startDate;

    public AccessToken(String appnexusToken, Date expiryDate)
    {
        this.appnexusToken = appnexusToken;
        this.startDate = expiryDate;
    }

    public String getAppnexusToken()
    {
        return appnexusToken;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    @Override
    public String toString()
    {
        return "AccessToken [appnexusToken=" + appnexusToken + ", expiryDate=" + startDate + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessToken)) return false;

        AccessToken that = (AccessToken) o;

        if (!appnexusToken.equals(that.appnexusToken)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = appnexusToken.hashCode();
        result = 31 * result + startDate.hashCode();
        return result;
    }
}
