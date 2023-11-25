package airhacks;

import airhacks.ec2.boundary.EC2;
import airhacks.ec2.boundary.EC2Stack;
import airhacks.vpc.boundary.VPCStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;



public class CDKApp {
    
    static class Builder{

    }

    public static void main(final String[] args) {

            var app = new App();
            var appName = "ec2";
            Tags.of(app).add("project", "powered by: airhacks.live");
            Tags.of(app).add("environment","development");
            Tags.of(app).add("application", appName);

            new EC2.Builder(app,appName)
                    .withNewVPC()
                    .build();

        app.synth();
    }
}
