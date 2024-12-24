package com.iocl.Dispatch_Portal_Application.composite_pk;
import java.io.Serializable;
import java.util.Objects;

import lombok.Data;
@Data
public class CourierContractId implements Serializable {

    private String locCode;
    private String courierContNo;

    public CourierContractId() {}

    public CourierContractId(String locCode, String courierContNo) {
        this.locCode = locCode;
        this.courierContNo = courierContNo;
    }

    // Getters, Setters, hashCode, equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourierContractId that = (CourierContractId) o;
        return Objects.equals(locCode, that.locCode) &&
               Objects.equals(courierContNo, that.courierContNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locCode, courierContNo);
    }

    // Getters and Setters
}
