package com.ti.acelera.plazoletamicroservice.configuration;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.DishMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.RestaurantMysqlAdapter;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
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

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantMysqlAdapter( restaurantRepository, restaurantEntityMapper );
    }
    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase( restaurantPersistencePort(), userClient());

    }
    @Bean
    public IDishPersistencePort dishPersistencePort(){
        return new DishMysqlAdapter(  );
    }
    @Bean
    public IDishServicePort dishServicePort(){
        return new DishUseCase( dishPersistencePort() );
    }

    private IUserClient userClient() {
        return new UserClientImpl();
    }

}
