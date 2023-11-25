package airhacks.ec2.boundary;

import java.util.List;

import airhacks.vpc.boundary.VPCStack;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.Instance;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.UserData;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

public class EC2Stack extends Stack {

        public EC2Stack(final Construct scope, String appName, IVpc vpc) {
                super(scope, appName);
                this.init(vpc);
        }

        public EC2Stack(final Construct scope, String appName, String vpcId) {
                super(scope, appName);
                var vpc = VPCStack.fetchExisting(this, vpcId);
                this.init(vpc);
        }

        void init(IVpc vpc) {
                var role = createRoleForSessionManager();
                // https://docs.aws.amazon.com/systems-manager/latest/userguide/agent-install-al2.html
                var userData = UserData.forLinux();
                userData.addCommands("sudo yum -y install java-17-amazon-corretto-headless");
                userData.addCommands("sudo yum -y install docker");
                userData.addCommands("sudo groupadd docker");
                userData.addCommands("sudo usermod -aG docker ec2-user");
                userData.addCommands("sudo usermod -aG docker ssm-user");
                userData.addCommands("sudo systemctl enable docker.service");
                userData.addCommands("sudo systemctl start docker.service");
                userData.addCommands(
                                "sudo wget https://dlcdn.apache.org/maven/maven-3/3.9.2/binaries/apache-maven-3.9.2-bin.tar.gz -P /tmp");
                userData.addCommands("sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt");
                userData.addCommands("sudo ln -s /opt/apache-maven-3.9.2/bin/mvn /usr/bin");
                userData.addCommands(
                                "sudo echo \"export PATH=/opt/apache-maven-3.9.2/bin:$PATH\" > /home/ec2-user/developer.sh");

                var instance = createInstance(vpc, userData, role);
                CfnOutput.Builder.create(this, "InstanceIdOutput").value(instance.getInstanceId()).build();
                CfnOutput.Builder.create(this, "InstancePublicDNSOutput").value(instance.getInstancePublicDnsName())
                                .build();
        }

        Instance createInstance(IVpc vpc, UserData userData, Role role) {
                return Instance.Builder.create(this, "instance").allowAllOutbound(true)
                                .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
                                .instanceName("airhacks-workplace")
                                .role(role)
                                .vpcSubnets(SubnetSelection
                                                .builder()
                                                .subnetType(SubnetType.PUBLIC)
                                                .build())
                                .securityGroup(this.securityGroup(vpc))
                                .userData(userData)
                                .machineImage(MachineImage.latestAmazonLinux2())
                                .vpc(vpc).build();

        }

        SecurityGroup securityGroup(IVpc vpc) {
                var sg = SecurityGroup.Builder.create(this, "SecurityGroup")
                                .allowAllOutbound(true)
                                .vpc(vpc)
                                .securityGroupName("airhacks-workplace").build();
                sg.addIngressRule(Peer.anyIpv4(), Port.allTcp());
                return sg;
        }

        Role createRoleForSessionManager() {
                return Role.Builder.create(this, "ssm-instance-role")
                                .assumedBy(ServicePrincipal.Builder.create("ec2.amazonaws.com").build())
                                .roleName("AirhacksEC2SessionManager")
                                .managedPolicies(List.of(
                                                ManagedPolicy.fromAwsManagedPolicyName("AmazonSSMManagedInstanceCore")))
                                .build();
        }

}
