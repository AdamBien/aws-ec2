package airhacks.ec2.boundary;

import airhacks.vpc.boundary.VPCStack;
import software.constructs.Construct;

/**
 * EC2
 */
public class EC2 {

    private EC2(Builder builder) {
        var scope = builder.scope;
        var appName = builder.appName;
        var vpcStack = new VPCStack(scope,appName);
        new EC2Stack(scope,appName,vpcStack.getVPC());
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
    
        public Builder newVPC(boolean newVPC) {
            this.newVPC = newVPC;
            return this;
        }

        public Builder vpcId(String vpcId) {
            this.vpcId = vpcId;
            return this;
        }
        
        public EC2 build() {
            if(!newVPC && vpcId == null){
                throw new IllegalStateException("vpcId is required for existing VPC");
            }
            return new EC2(this);
        }
    }
}