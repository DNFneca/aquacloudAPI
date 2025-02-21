package cloud.aquacloud.aquacloudapi.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class NanoId implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return NanoIdUtils.randomNanoId();
    }
}
