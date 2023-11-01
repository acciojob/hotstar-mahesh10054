package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) != null)
        {
            throw new Exception("Series is already present");
        }

        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        int productionHouseId = webSeriesEntryDto.getProductionHouseId();
        ProductionHouse productionHouse = productionHouseRepository.findById(productionHouseId).get();
        webSeries.setProductionHouse(productionHouse);
        productionHouse.getWebSeriesList().add(webSeries);

        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();
        double productionHouseRating = calculateProductionHouseRating(webSeriesList);
        productionHouse.setRatings(productionHouseRating);

        webSeriesRepository.save(webSeries);
        return webSeries.getId();
    }

    public double calculateProductionHouseRating(List<WebSeries> webSeriesList)
    {
        double rating = 0;
        int movieCount = webSeriesList.size();

        for(WebSeries webSeries : webSeriesList)
        {
            double webSeriesRating = webSeries.getRating();
            rating += webSeriesRating;
        }
        return rating/movieCount;
    }

}












