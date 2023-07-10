package com.zerobase.stockdividend.scraper;

import com.zerobase.stockdividend.model.Company;
import com.zerobase.stockdividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
