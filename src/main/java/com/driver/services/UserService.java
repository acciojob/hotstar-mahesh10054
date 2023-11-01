package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        if(userRepository.findById(user.getId()).get() != null) return 0;
        userRepository.save(user);

        return user.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user = userRepository.findById(userId).get();
        int age = user.getAge();

        String subscriptionType = user.getSubscription().getSubscriptionType().toString();
        int count = 0;
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        for(WebSeries webSeries : webSeriesList)
        {
            int ageLimit = webSeries.getAgeLimit();
            String subscriptionRequired = webSeries.getSubscriptionType().toString();

            if(age >= ageLimit && checkSubscription(subscriptionRequired,subscriptionType))
            {
                count++;
            }
        }
        return count;
    }
    public boolean checkSubscription(String subscriptionRequired,String subscriptionType)
    {
        int s = 0;
        int n = 0;
        if(subscriptionRequired.equals("BASIC"))
        {
            s = 1;
        } else if (subscriptionRequired.equals("PRO")) {
            s = 2;
        } else if (subscriptionRequired.equals("ELITE")){
            s = 3;
        }

        if(subscriptionType.equals("BASIC"))
        {
            n = 1;
        } else if (subscriptionType.equals("PRO")) {
            n = 2;
        } else if (subscriptionType.equals("ELITE")) {
            n = 3;
        }

        if(n != 0 && s != 0 && n >= s)
        {
            return true;
        }

        return false;
    }


}
