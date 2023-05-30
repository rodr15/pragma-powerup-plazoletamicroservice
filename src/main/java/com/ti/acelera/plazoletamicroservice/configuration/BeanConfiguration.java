package com.ti.acelera.plazoletamicroservice.configuration;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.DishMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.RestaurantMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter.UserClientImpl;
import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
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

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantMysqlAdapter( restaurantRepository, restaurantEntityMapper );
    }
    @Bean
    public IRestaurantServicePort restaurantServicePort( IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IUserClient userClient){
        return new RestaurantUseCase( restaurantPersistencePort, dishPersistencePort,userClient);

    }
    @Bean
    public IDishPersistencePort dishPersistencePort(){
        return new DishMysqlAdapter( dishRepository, dishEntityMapper);
    }
    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantServicePort ,IUserClient userClient ){
        return new DishUseCase( dishPersistencePort, restaurantServicePort, userClient);
    }
    @Bean
    public IUserClient userClient() {
        return new UserClientImpl();
    }

}
