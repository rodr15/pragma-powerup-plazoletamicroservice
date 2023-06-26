package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.CategoryAveragePrice;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDishPersistencePort {
    void saveDish(Dish dish);

    Optional<Dish> getDish(Long id);

    Page<Dish> getActiveDishesByRestaurantId(Long restaurantId, int page, int size);

    Page<Dish> getActiveDishesByRestaurantId(Long restaurantId, Long categoryId, int page, int size);
    List<Dish> findAllDishesByIdAndByRestaurantId( Long restaurantId ,List<Long> dishesId);
    List<CategoryAveragePrice> dishCategoryAveragePrice(Long restaurantId);
    Page<Dish> getDishesByBudgetAndCategoryPreferences(Long budget, List<Long> categoryPreferencesId, Pageable pageable);
}
