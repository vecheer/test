package serialize.copyobject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yq
 * @version v1.0 2023-01-16 8:29 PM
 */
@Data
@AllArgsConstructor
public class User implements Serializable {
    private final String id;
    private final String name;
    private final UserAddress address;
}
