package org.once_a_day.sso.mapper.impl;

import org.once_a_day.sso.mapper.FileMapper;
import org.springframework.stereotype.Component;

@Component
public class FileMapperImpl implements FileMapper {
    @Override
    public String getUrl(Long fileId) {
        return "http://x.x.x.x:8081/files/" + fileId;
    }
}
