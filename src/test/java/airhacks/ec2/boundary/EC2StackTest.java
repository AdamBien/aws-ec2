package airhacks.ec2.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EC2StackTest {

    @Test
    void existingVpcWithoutId(){
        var exception = assertThrows(RuntimeException.class,() -> new EC2Stack.Builder(null,"ec2").build());
        assertThat(exception.getMessage()).containsSequence("vpcId is required for existing VPC");
    }
}
