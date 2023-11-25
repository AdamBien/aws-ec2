package airhacks;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public interface Configuration {

    static StackProps stackProperties(String region,String account) {
        var env = Environment.builder()
                .account(account)
                .region(region)
                .build();
        return StackProps.builder()
                .env(env)
                .build();
    }

}
