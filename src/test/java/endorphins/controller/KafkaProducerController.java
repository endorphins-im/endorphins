package endorphins.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author timothy
 * @createTime 2022年02月06日 11:48:00
 */
@RestController
public class KafkaProducerController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @GetMapping("/send")
    public String sendMsg(){
        kafkaTemplate.send("topic-tim01",0, "key", "this is a msg!");
        return "send success";
    }
}
