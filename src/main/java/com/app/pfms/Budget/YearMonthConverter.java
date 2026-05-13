package com.app.pfms.Budget;
import java.time.YearMonth;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, String> {
    //Convert YearMonth to String before saving to DB
    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        //If value is null, store null
        //Otherwise, convert YearMonth to String
        return attribute == null ? null : attribute.toString(); 
    }

    //Convert Stirng from DB back to YearMonth
    @Override   
    public YearMonth convertToEntityAttribute(String dbData) {
        //If DB value is null, return null
        //Otherwise, parse String into YearMonth object
        return dbData == null ? null : YearMonth.parse(dbData);
    }
}