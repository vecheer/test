package serialize.copyobject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yq
 * @version v1.0 2023-01-16 8:30 PM
 */
@Data
@AllArgsConstructor
public class UserAddress implements Serializable {
    private final String country;
    private final String province;
    private final String city;
    private final String region;
}
