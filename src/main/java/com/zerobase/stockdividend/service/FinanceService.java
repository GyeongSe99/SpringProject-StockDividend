package com.zerobase.stockdividend.service;

import com.zerobase.stockdividend.exception.impl.NoCompanyException;
import com.zerobase.stockdividend.model.Company;
import com.zerobase.stockdividend.model.Dividend;
import com.zerobase.stockdividend.model.ScrapedResult;
import com.zerobase.stockdividend.model.constants.CacheKey;
import com.zerobase.stockdividend.persist.CompanyRepository;
import com.zerobase.stockdividend.persist.DividendRepository;
import com.zerobase.stockdividend.persist.entity.CompanyEntity;
import com.zerobase.stockdividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // @Cacheable
    // 캐시에 데이터가 없는 경우 리턴값을 캐시에 추가
    // 캐시에 데이터가 있는 경우 로직을 실행시키지 않고 캐시의 데이터 반환
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        // 1. 회사명을 기준으로 회사 정보 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        // 2. 조회된 회사 아이디로 배당금 정보 조회
        List<DividendEntity> dividendEntities =
                this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                                            .map(e -> new Dividend(e.getDate(), e.getDividend()))
                                            .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName()), dividends);
    }
}
