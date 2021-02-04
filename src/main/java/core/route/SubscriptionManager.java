package core.route;

import com.google.gson.Gson;
import core.Request;
import core.Response;
import core.Subscription;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SubscriptionManager {

    static HashMap<String, String[]> endPointTriggers = new HashMap<>();
    static HashMap<String, ArrayList<Subscription>> subscriptionMap = new HashMap<>();

    static {
        String[] createBidTriggers = {"getProduct"};
        endPointTriggers.put("createBid", createBidTriggers);

        String[] updateProductTriggers = {"getProduct"};
        endPointTriggers.put("updateProduct", updateProductTriggers);
    }

    public static synchronized void saveSubscription(Subscription subscription) {
        if (subscriptionMap.get(subscription.request.identifier) == null) {
            ArrayList<Subscription> subscriptions = new ArrayList<>();
            subscriptions.add(subscription);
            subscriptionMap.put(subscription.request.identifier, subscriptions);
        } else {
            subscriptionMap.get(subscription.request.identifier).add(subscription);
        }
    }

    private static synchronized void removeSubscription(Subscription subscription) {
        if (subscriptionMap.get(subscription.request.identifier) != null) {
            ArrayList<Subscription> subscriptions = subscriptionMap.get(subscription.request.identifier);
            subscriptions.remove(subscription);
            subscriptionMap.put(subscription.request.identifier, subscriptions);
        }
    }

    public static synchronized void triggerSubscriptions(String identifier) {
        String[] triggeredEndpoints = endPointTriggers.get(identifier);
        for (String endpoint : triggeredEndpoints) {
            ArrayList<Subscription> subscriptions = subscriptionMap.get(endpoint);
            for (Subscription subscription : subscriptions) {
                handleSubscription(subscription);
            }
        }
    }

    private static void handleSubscription(Subscription subscription) {
        new Thread(() -> {
            Gson gson = new Gson();
            try {
                System.out.println("Handle subscription of: " + subscription.request.identifier);
                Request request = subscription.request;
                DataOutputStream outputStream = subscription.outputStream;

                Response response = Router.routeRequest(request);
                String responseJson = gson.toJson(response);

                outputStream.writeBytes(responseJson + '\n');
            } catch (IOException e) {
                e.printStackTrace();
                removeSubscription(subscription);
            }
        }).start();
    }
}
