package org.trails.demo;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Demographics
 *
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */
@Embeddable
public class Demographics implements Serializable {
    private static final Log log = LogFactory.getLog(Demographics.class);

    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String dayPhone;
    private String cellPhone;
    private String website;

    /**
     * Copy CTOR
     */
    public Demographics(Demographics dto) {
        try {
            BeanUtils.copyProperties(this, dto);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    public Demographics() {
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getDayPhone() {
        return dayPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDayPhone(String dayPhone) {
        this.dayPhone = dayPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return getCity() + ", " + getState();
    }

    @Override
    public Demographics clone() {
        return new Demographics(this);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((address == null) ? 0 : address.hashCode());
        result = PRIME * result + ((cellPhone == null) ? 0 : cellPhone.hashCode());
        result = PRIME * result + ((city == null) ? 0 : city.hashCode());
        result = PRIME * result + ((country == null) ? 0 : country.hashCode());
        result = PRIME * result + ((dayPhone == null) ? 0 : dayPhone.hashCode());
        result = PRIME * result + ((state == null) ? 0 : state.hashCode());
        result = PRIME * result + ((website == null) ? 0 : website.hashCode());
        result = PRIME * result + ((zip == null) ? 0 : zip.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Demographics other = (Demographics) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (cellPhone == null) {
            if (other.cellPhone != null)
                return false;
        } else if (!cellPhone.equals(other.cellPhone))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (dayPhone == null) {
            if (other.dayPhone != null)
                return false;
        } else if (!dayPhone.equals(other.dayPhone))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (website == null) {
            if (other.website != null)
                return false;
        } else if (!website.equals(other.website))
            return false;
        if (zip == null) {
            if (other.zip != null)
                return false;
        } else if (!zip.equals(other.zip))
            return false;
        return true;
    }
}