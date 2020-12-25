package api;

import java.util.Objects;

public class GeoLocation implements geo_location{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    private double x, y, z;

    public GeoLocation()
    {
        x = 0;
        y = 0;
        z = 0;
    }
    public GeoLocation(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public GeoLocation(geo_location geo) {
        if(geo == null)
        {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            return;
        }
        this.x = geo.x();
        this.y = geo.y();
        this.z = geo.z();
    }


    public void setLocation(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {

        double xDist = Math.pow(g.x() - x, 2);
        double yDist = Math.pow(g.y() - y, 2);
        double zDist = Math.pow(g.z() - z, 2);

        return Math.sqrt(xDist + yDist + zDist);
    }
}
