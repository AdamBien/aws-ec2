package airhacks;

import airhacks.ec2.boundary.EC2Stack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;



public class CDKApp {
    

    public static void main(final String[] args) {

            var app = new App();
            var appName = "ec2";
            Tags.of(app).add("project", "powered by: airhacks.live");
            Tags.of(app).add("environment","development");
            Tags.of(app).add("application", appName);

            new EC2Stack.Builder(app,appName)
                    .withNewVPC()
                    .build();
        app.synth();
    }
}
