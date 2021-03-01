package com.nateprat.equakers.model;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {

    private final String name;
    private final String county;
    private final LatLng latLng;

    public Location(String name, String county, LatLng latLng) {
        this.name = name;
        this.county = county;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public String getCounty() {
        return county;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getLocationString() {
        StringBuilder sb = new StringBuilder();
        sb.append(WordUtils.capitalizeFully(name));
        if (county != null) {
            sb.append(", ");
            sb.append(WordUtils.capitalizeFully(county));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", county='" + county + '\'' +
                ", latLng=" + latLng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(name, location.name) &&
                Objects.equals(county, location.county) &&
                Objects.equals(latLng, location.latLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, county, latLng);
    }
}
