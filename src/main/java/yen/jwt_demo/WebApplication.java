package yen.jwt_demo;

import yen.jwt_demo.repository.UserRepository;
import yen.jwt_demo.service.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Database database;

    @PostConstruct
    public void initData(){
        //系統啟動時將預設假資料塞入mongodb
        userRepository.saveAll(database.getData());
    }
}
