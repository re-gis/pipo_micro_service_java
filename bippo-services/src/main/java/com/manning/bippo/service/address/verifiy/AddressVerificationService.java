package com.manning.bippo.service.address.verifiy;

import com.manning.bippo.dao.itf.Addressable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AddressVerificationService
{
    AddressVerificationResponse query(String address) throws IOException;

    public default AddressVerificationResponse query(Addressable address) throws IOException {
        return this.query(address == null ? null : address.getAddress());
    }

    List<? extends AddressVerificationResponse> queryMultiple(Collection<String> addresses);

    <K> Map<K, ? extends AddressVerificationResponse> queryMultipleMapped(Map<K, String> properties);
}
