package airhacks.vpc.boundary;

import java.util.List;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Tags;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.InterfaceVpcEndpointAwsService;
import software.amazon.awscdk.services.ec2.InterfaceVpcEndpointOptions;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.constructs.Construct;

public class VPCStack extends Stack {

        private Vpc vpc;

        public VPCStack(Construct scope, String appName) {
                super(scope, appName + "-vpc");

                this.vpc = Vpc.Builder.create(this, "vpc")
                                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                                .enableDnsHostnames(true)
                                .enableDnsSupport(true)
                                .maxAzs(1)
                                .subnetConfiguration(List.of(SubnetConfiguration.builder()
                                                .subnetType(SubnetType.PUBLIC)
                                                .name(appName)
                                                .cidrMask(18)
                                                .build()))
                                .natGateways(0)
                                .build();

                Tags.of(this.vpc).add("Name", appName);
                addEndpoints(vpc);
        }

        public static void addEndpoints(IVpc vpc) {
                vpc.addInterfaceEndpoint("ssm", InterfaceVpcEndpointOptions.builder()
                                .service(InterfaceVpcEndpointAwsService.SSM)
                                .privateDnsEnabled(true)
                                .build());

                vpc.addInterfaceEndpoint("ssmmessages", InterfaceVpcEndpointOptions.builder()
                                .service(InterfaceVpcEndpointAwsService.SSM_MESSAGES)
                                .privateDnsEnabled(true)
                                .build());

                vpc.addInterfaceEndpoint("ec2Messages", InterfaceVpcEndpointOptions.builder()
                                .service(InterfaceVpcEndpointAwsService.EC2_MESSAGES)
                                .privateDnsEnabled(true)
                                .build());
        }

        public static IVpc fetchExisting(Construct scope, String vpcId) {
                var vpc =  Vpc.fromLookup(scope, "vpc", VpcLookupOptions
                                .builder()
                                .vpcId(vpcId)
                                .build());
                addEndpoints(vpc);
                return vpc;
        }

        public IVpc getVPC() {
                return this.vpc;
        }

}
