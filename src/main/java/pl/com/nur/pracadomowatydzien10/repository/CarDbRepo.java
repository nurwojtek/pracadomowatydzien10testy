package pl.com.nur.pracadomowatydzien10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.nur.pracadomowatydzien10.model.Car;

import java.util.List;

@Repository
public interface CarDbRepo extends JpaRepository<Car, Long> {

    List<Car> findByYearProductionBetween(int min, int max);
    Car getFirstByMarkEquals(String search);
}
