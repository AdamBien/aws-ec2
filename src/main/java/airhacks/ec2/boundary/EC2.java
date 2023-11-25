package airhacks.ec2.boundary;

import airhacks.vpc.boundary.VPCStack;
import software.amazon.awscdk.services.ec2.IVpc;
import software.constructs.Construct;

/**
 * EC2
 */
public class EC2 {

    private EC2(Builder builder) {
        var scope = builder.scope;
        var appName = builder.appName;
        IVpc vpc = null;

        if(builder.newVPC){
            var vpcStack = new VPCStack(scope,appName);
            vpc = vpcStack.getVPC();
        }else{
            vpc = VPCStack.fetchExisting(scope,builder.vpcId);
        }
        new EC2Stack(scope,appName,vpc);
    }

    public static class Builder {
        boolean newVPC;
        String vpcId;
        String appName;
        Construct scope;

        public Builder(Construct construct,String appName) {
            this.scope = construct;
            this.appName = appName;
        }
    
        public Builder withNewVPC() {
            this.newVPC = true;
            return this;
        }

        public Builder vpcId(String vpcId) {
            this.vpcId = vpcId;
            return this;
        }
        
        public EC2 build() {
            if(!newVPC && vpcId == null){
                throw new RuntimeException("vpcId is required for existing VPC",null,false,false){};
            }
            return new EC2(this);
        }
    }
}