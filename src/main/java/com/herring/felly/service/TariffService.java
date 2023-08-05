package com.herring.felly.service;

import com.herring.felly.document.TariffDocument;
import com.herring.felly.enums.TariffType;
import com.herring.felly.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;

    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public TariffDocument getTariffByName(String name) {
        return tariffRepository.findFirstByName(name).orElse(null);
    }

    public List<TariffDocument> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public List<TariffDocument> getAllTariffsByType(TariffType type) {
        return tariffRepository.findAllByType(type);
    }

    @Transactional
    public TariffDocument createTariff(TariffDocument tariff) {
        return tariffRepository.save(tariff);
    }
}
