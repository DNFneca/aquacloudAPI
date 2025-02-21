package cloud.aquacloud.aquacloudapi;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class AquacloudApiApplication {


    @Value("${stripe.api.secretKey}")
    private static String stripeApiKey = "sk_test_51QqGlf2fZbB2PD4LIj6UzLXDBj3fJN2ExKiDriK8WNs8LWSMOsR9cH85yG3KKTY5N2RQaaknb7Zzljtse8rs6cTY00QLYyjyMq";

    public static void main(String[] args) {
        System.out.println(stripeApiKey);
        Stripe.apiKey = stripeApiKey;
        SpringApplication.run(AquacloudApiApplication.class, args);
    }

}
