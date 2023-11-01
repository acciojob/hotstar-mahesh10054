package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){
        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription = new Subscription();

        String subscriptionType = subscriptionEntryDto.getSubscriptionType().toString();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        int noOfScreensSubscribed = subscriptionEntryDto.getNoOfScreensRequired();
        subscription.setNoOfScreensSubscribed(noOfScreensSubscribed);

        int total = 0;
        if(subscriptionType.equals("BASIC")){
            total = 500 + 200*noOfScreensSubscribed;
        }
        else if(subscriptionType.equals("PRO")) {
            total = 800 + 250*noOfScreensSubscribed;
        }
        else {
            total = 1000 + 350*noOfScreensSubscribed;
        }
        subscription.setTotalAmountPaid(total);

        int userId = subscriptionEntryDto.getUserId();
        User user = userRepository.findById(userId).get();

        subscription.setUser(user);

        subscriptionRepository.save(subscription);
        return total;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();

        String subscriptionType = subscription.getSubscriptionType().toString();
        int totalAmountPaid = subscription.getTotalAmountPaid();
        int noOfScreensSubscribed = subscription.getNoOfScreensSubscribed();
        int total = 0;

        if(subscriptionType.equals("ELITE"))
        {
            throw new Exception("Already the best Subscription");
        } else if((subscriptionType.equals("BASIC")))
        {
            subscription.setSubscriptionType(SubscriptionType.PRO);
            total = (800 + 250*noOfScreensSubscribed) - totalAmountPaid;
        }
        else {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            total = (1000 + 350*noOfScreensSubscribed) - totalAmountPaid;
        }
        subscription.setTotalAmountPaid(total);
        userRepository.save(user);
        return total;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int totalRevenue = 0;
        List<Subscription> subscriptionList = subscriptionRepository.findAll();

        for(Subscription subscription : subscriptionList)
        {
            int totalAmountPaid = subscription.getTotalAmountPaid();
            totalRevenue += totalAmountPaid;
        }

        return totalRevenue;
    }

}
