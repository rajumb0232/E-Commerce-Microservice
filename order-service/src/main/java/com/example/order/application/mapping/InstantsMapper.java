package com.example.order.application.mapping;

import java.time.Instant;

public interface InstantsMapper {

    default Long toEpochMillis(Instant instant){
        if(instant == null)
            return null;

        return instant.toEpochMilli();
    }

    default Instant toInstant(Long epochMillis){
        if(epochMillis == null)
            return null;

        return Instant.ofEpochMilli(epochMillis);
    }
}
