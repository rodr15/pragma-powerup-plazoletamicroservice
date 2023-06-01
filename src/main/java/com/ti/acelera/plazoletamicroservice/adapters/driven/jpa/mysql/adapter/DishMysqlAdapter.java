package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DishMysqlAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void saveDish(Dish dish) {
        dishRepository.save(dishEntityMapper.toEntity(dish));
    }

    @Override
    public Optional<Dish> getDish(Long id) {
        Optional<DishEntity> dish = dishRepository.findById(id);

        if (dish.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(dishEntityMapper.toDish(dish.get()));
    }

    @Override
    public Page<Dish> getActiveDishesByRestaurantId(Long restaurantId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DishEntity> dishEntityPage = dishRepository.findByRestaurantIdAndActiveTrue(restaurantId, pageable);

        return dishEntityPage.map(dishEntityMapper::toDish);
    }

    @Override
    public Page<Dish> getActiveDishesByRestaurantId(Long restaurantId, Long categoryId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DishEntity> dishEntityPage = dishRepository.findByRestaurantIdAndCategoryIdAndActiveTrue(restaurantId, categoryId, pageable);

        return dishEntityPage.map(dishEntityMapper::toDish);
    }

    @Override
    public List<Dish> findAllDishesByIdAndByRestaurantId(Long restaurantId, List<Long> dishesId) {
        List<DishEntity> dishEntityList = dishRepository.findAllByIdInAndRestaurantIdAndActiveTrue(dishesId, restaurantId);
        return dishEntityList.stream().map(dishEntityMapper::toDish).toList();

    }
}
