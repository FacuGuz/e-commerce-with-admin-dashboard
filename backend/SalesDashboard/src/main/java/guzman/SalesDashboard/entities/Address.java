package guzman.SalesDashboard.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Address other = (Address) obj;
        if (street == null) {
            if (other.street != null) return false;
        } else if (!street.equals(other.street)) return false;
        
        if (city == null) {
            if (other.city != null) return false;
        } else if (!city.equals(other.city)) return false;
        
        if (state == null) {
            if (other.state != null) return false;
        } else if (!state.equals(other.state)) return false;
        
        if (zipCode == null) {
            if (other.zipCode != null) return false;
        } else if (!zipCode.equals(other.zipCode)) return false;
        
        if (country == null) {
            if (other.country != null) return false;
        } else if (!country.equals(other.country)) return false;
        
        return true;
    }
}
