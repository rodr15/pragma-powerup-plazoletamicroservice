package com.ti.acelera.plazoletamicroservice.configuration;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.DishMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.DishOrderMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.OrderRestaurantMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.RestaurantMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.*;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishOrderRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IOrderRestaurantRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter.SmsClientImpl;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter.TraceabilityClientImpl;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter.UserClientImpl;
import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ISmsClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishOrderPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.transactions.DeleteRestaurantsTransaction;
import com.ti.acelera.plazoletamicroservice.domain.usecase.DishUseCase;
import com.ti.acelera.plazoletamicroservice.domain.usecase.RestaurantUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IOrderRestaurantRepository orderRestaurantRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IDishOrderEntityMapper dishOrderEntityMapper;
    private final IDishOrderRepository dishOrderRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantMysqlAdapter( restaurantRepository, restaurantEntityMapper );
    }
    @Bean
    public IOrderRestaurantPersistencePort orderRestaurantPersistencePort(){
        return new OrderRestaurantMysqlAdapter(orderRestaurantRepository, orderEntityMapper, dishOrderRepository, dishOrderEntityMapper);
    }
    @Bean
    public DeleteRestaurantsTransaction transaction(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IDishPersistencePort dishPersistencePort,
                                                        IOrderRestaurantPersistencePort orderRestaurantPersistencePort,
                                                        IDishOrderPersistencePort dishOrderPersistencePort,
                                                        ITraceabilityClient traceabilityClient
                                                        ){
        return new DeleteRestaurantsTransaction(restaurantPersistencePort, dishPersistencePort, orderRestaurantPersistencePort, dishOrderPersistencePort, traceabilityClient);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IDishPersistencePort dishPersistencePort,
                                                        IOrderRestaurantPersistencePort orderRestaurantPersistencePort,
                                                        IDishOrderPersistencePort dishOrderPersistencePort,
                                                        IUserClient userClient,
                                                        ITraceabilityClient traceabilityClient,
                                                        ISmsClient smsClient,
                                                        DeleteRestaurantsTransaction transaction){
        return new RestaurantUseCase(restaurantPersistencePort, dishPersistencePort, orderRestaurantPersistencePort, dishOrderPersistencePort, userClient,traceabilityClient,smsClient,transaction);
    }
    @Bean
    public IDishPersistencePort dishPersistencePort(IDishRepository dishRepository, IDishEntityMapper dishEntityMapper, ICategoryEntityMapper categoryEntityMapper){
        return new DishMysqlAdapter( dishRepository, dishEntityMapper,categoryEntityMapper);
    }
    @Bean
    public IDishOrderPersistencePort dishOrderPersistencePort() {
        return new DishOrderMysqlAdapter(dishOrderRepository, dishOrderEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantServicePort ,IUserClient userClient ){
        return new DishUseCase( dishPersistencePort, restaurantServicePort, userClient);
    }

    @Bean
    public IUserClient userClient() {return new UserClientImpl();}
    @Bean
    public ISmsClient smsClient() {return new SmsClientImpl();}

    @Bean
    public ITraceabilityClient traceabilityClient() {
        return new TraceabilityClientImpl();
    }

}
