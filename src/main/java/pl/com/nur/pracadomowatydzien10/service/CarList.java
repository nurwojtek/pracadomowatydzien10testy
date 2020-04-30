package pl.com.nur.pracadomowatydzien10.service;

import org.springframework.stereotype.Service;
import pl.com.nur.pracadomowatydzien10.model.Car;
import pl.com.nur.pracadomowatydzien10.repository.CarDbRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarList {

    private CarDbRepo carDbRepo;
    private List<Car> carList;

    public CarList(CarDbRepo carDbRepo) {
        carList = new ArrayList<>();
        this.carDbRepo = carDbRepo;
//        addCar(new Car("Volvo", "V70", "Czerwony", 2000));
//        addCar(new Car("Volvo", "V90", "Szary", 1995));
//        addCar(new Car("Fiat", "126p", "Kanarkowy", 1980));
    }

    public List<Car> getCarList(){
        return carDbRepo.findAll();
    }

    public void addCar(Car car){
        carDbRepo.save(car);
    }

    public void modCar(Car car){
        carDbRepo.saveAndFlush(car);
    }

    public void delCar(Long id){
        carDbRepo.deleteById(id);
    }

    public List<Car> searchCarYearProductionBetween(int yearMin, int yearMax){
        return carDbRepo.findByYearProductionBetween(yearMin, yearMax);
    }

    public Car getOneCarMark(String mark){
        return carDbRepo.getFirstByMarkEquals(mark);
    }


}
