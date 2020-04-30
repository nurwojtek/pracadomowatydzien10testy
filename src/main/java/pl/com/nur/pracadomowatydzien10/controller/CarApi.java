package pl.com.nur.pracadomowatydzien10.controller;

import org.springframework.web.bind.annotation.*;
import pl.com.nur.pracadomowatydzien10.model.Car;
import pl.com.nur.pracadomowatydzien10.service.CarList;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private CarList carList;

    public CarApi(CarList carList) {
        this.carList = carList;
    }

    @GetMapping("/test")
    public String getTest(){
        return "test";
    }

    @GetMapping
    public List<Car> getCarList(){
        return carList.getCarList();
    }

    @PostMapping("/add")
    public Car addNewCar(@RequestBody Car car) {
        System.out.println(car);
        carList.addCar(car);
        return car;
    }

    @PutMapping("/mod")
    public Car modCar(@RequestBody Car car) {
        carList.modCar(car);
        return car;
    }

    @PostMapping("/search")
    public List<Car> modCar(@RequestParam int yearMin,
                            @RequestParam int yearMax){
        return carList.searchCarYearProductionBetween(yearMin, yearMax);
    }

    @DeleteMapping("/del/{id}")
    public void delCar(@PathVariable Long id){
        carList.delCar(id);
    }

}
