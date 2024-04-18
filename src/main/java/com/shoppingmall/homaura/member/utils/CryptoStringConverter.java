package com.shoppingmall.homaura.member.utils;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoStringConverter implements AttributeConverter<String, String> {

    private final TwoWayEncryption twoWayEncryption;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return twoWayEncryption.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return twoWayEncryption.decrypt(dbData);
    }
}
