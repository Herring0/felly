package com.herring.felly.service.log;

import com.herring.felly.document.log.ApiLogRequestDocument;
import com.herring.felly.document.log.ApiLogResponseDocument;
import com.herring.felly.repository.log.ApiLogRequestRepository;
import com.herring.felly.repository.log.ApiLogResponseRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiLogService {
    private final ApiLogRequestRepository apiLogRequestRepository;
    private final ApiLogResponseRepository apiLogResponseRepository;

    public ApiLogService(ApiLogRequestRepository apiLogRequestRepository, ApiLogResponseRepository apiLogResponseRepository) {
        this.apiLogRequestRepository = apiLogRequestRepository;
        this.apiLogResponseRepository = apiLogResponseRepository;
    }

    public void writeLog(ApiLogRequestDocument apiLogRequestDocument) {
        apiLogRequestRepository.save(apiLogRequestDocument);
    }

    public void writeLog(ApiLogResponseDocument apiLogResponseDocument) {
        apiLogResponseRepository.save(apiLogResponseDocument);
    }
}
