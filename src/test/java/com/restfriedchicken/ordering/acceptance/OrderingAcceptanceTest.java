package com.restfriedchicken.ordering.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import com.restfriedchicken.ordering.rest.OrderResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class OrderingAcceptanceTest {


    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void should_suggest_payment_when_an_order_has_been_placed() throws Exception {
        final String trackingId = UUID.randomUUID().toString();
        final PlaceOrderCommand command = new PlaceOrderCommand(
                trackingId, new PlaceOrderCommand.Item("fried chicken", 1));

        final OrderResource resource = restTemplate.
                postForObject("http://localhost:8080/order", command, OrderResource.class);

        assertThat(resource.getLink("payment").getHref(), is("http://www.restfriedchicken.com/online-txn/" + trackingId));
    }

    @Configuration
    @EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
    static class ContextConfiguration {

        @Bean
        RestTemplate restTemplate() {
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(halConverter());

            RestTemplate restTemplate = new RestTemplate();

            restTemplate.setMessageConverters(converters);

            return restTemplate;
        }

        @Autowired
        private RelProvider relProvider;

        @Bean
        public HttpMessageConverter halConverter() {
            ObjectMapper halObjectMapper = new ObjectMapper();
            halObjectMapper.registerModule(new Jackson2HalModule());
            halObjectMapper
                    .setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(relProvider, null));

            MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();
            halConverter.setObjectMapper(halObjectMapper);
            return halConverter;
        }
    }


}
