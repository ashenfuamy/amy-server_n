package site.ashenstation.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.app.dto.CreateSerialDto;

@Service
@RequiredArgsConstructor
public class SerialService {

    @UpdateResourceCache
    @Transactional
    public void createSerial(CreateSerialDto dto) {
    }
}
