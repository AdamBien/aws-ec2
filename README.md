# EC2 with SSM for deployment into a new, or exising VPC

This is a blank, slightly streamlined, project for [EC2 provisioning with AWS CDKv2](https://docs.aws.amazon.com/cdk/latest/guide/work-with-cdk-java.html).

You will find the maven command in `cdk.json` file.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Installation

0. For max convenience use the [`default` profile](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html)
1. Install [AWS CDK CLI](https://docs.aws.amazon.com/cdk/latest/guide/getting_started.html)
2. [`cdk boostrap --profile YOUR_AWS_PROFILE`](https://docs.aws.amazon.com/cdk/latest/guide/bootstrapping.html)

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

## in action

### Infrastructure as Java Code (IaJC): Setting AWS System Manager Parameter

[![Infrastructure as Java Code (IaJC): Setting AWS System Manager Parameter](https://i.ytimg.com/vi/eTG7EV1ThqQ/mqdefault.jpg)](https://www.youtube.com/embed/eTG7EV1ThqQ?rel=0)

## references

Used in: 

1. [Plain Lambda Template](https://github.com/AdamBien/aws-lambda-cdk-plain)
2. [MicroProfile on Quarkus as Lambda Template](https://github.com/AdamBien/aws-quarkus-lambda-cdk-plain)


See you at: [airhacks.live](https://airhacks.live)
