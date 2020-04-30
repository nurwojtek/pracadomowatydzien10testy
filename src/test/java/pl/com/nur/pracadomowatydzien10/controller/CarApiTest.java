package pl.com.nur.pracadomowatydzien10.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.com.nur.pracadomowatydzien10.model.Car;
import pl.com.nur.pracadomowatydzien10.service.CarList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CarApiTest {


    @Autowired
    private MockMvc mockMvc;  // robimy wsprzyknięcie do pola ale to są testy i tak się robi

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();
//    @MockBean
    @Autowired
    private CarList carList;

    @Test
    void should_get_test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }

    @Test
    void should_get_CarList() throws Exception {
        Long idCar = 0L;
        String expression = "$.[" + idCar+ "].mark";
        int sizeList = carList.getCarList().size();
        System.out.println(sizeList + " ilosc w bazie");
        mockMvc.perform(MockMvcRequestBuilders.get("/cars")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath(expression).value("Volvo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].mark").value("Volvo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].mark").value("Fiat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(sizeList)));
    }

    @Test
    void should_add_new_Car() throws Exception {
        String url = "/cars/add";
        int sizeList = carList.getCarList().size();
        Car car = new Car("TestowaMarka1975", "Model", "Zielony", 2222);
        String jsonRequest = objectMapper.writeValueAsString(car);
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("TestowaMarka1975"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("Zielony"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yearProduction").value(2222));

        Assert.assertEquals(sizeList+1, carList.getCarList().size());

        if(sizeList<carList.getCarList().size()) {  // jeżeli dodało nam car to go kasujemy
            Car newCar = carList.getOneCarMark("TestowaMarka1975");
            carList.delCar(newCar.getCarId());
        }
    }

    @Test
    void should_mod_Car() throws Exception {
        String url = "/cars/mod";
        Car car = new Car("Volvo", "V70", "Czerwony", 1995);
        car.setCarId(1L);
        String jsonRequest = objectMapper.writeValueAsString(car);
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("Volvo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("V70"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("Czerwony"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yearProduction").value(1995));
    }

    @Test
    void should_search_CarList_dateMin_dateMax() throws Exception {
        String url = "/cars/search?yearMin={MIN}&yearMax={MAX}";
        mockMvc.perform(MockMvcRequestBuilders.post(url, 1980, 1994)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].carId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].mark").value("Fiat"));
    }

    @Test
    void should_del_Car() throws Exception {
        Car car = new Car("VolvoTestowe1234", "V70", "Czerwony", 1995);
        carList.addCar(car);
        carList.getOneCarMark("VolvoTestowe1234");
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cars/del/{id}", car.getCarId()))
                .andExpect(status().isOk());
    }
}