package endorphins.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @author timothy
 * @createTime 2022年02月06日 12:20:00
 */
@Component
public class MyConsumer {

    /**
     * 监听一批 records
     * @param records
     * @param ack
     */
    @KafkaListener(topics = "topic-tim01", groupId = "")
    public void listenGroups(ConsumerRecords<String, String> records, Acknowledgment ack) {
        Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
        while (iterator.hasNext()) {
            // TODO: 2022/2/6 balabala
        }
    }

    /**
     * 监听一个 record
     * @param record
     * @param ack
     */
    @KafkaListener(topics = "topic-tim01", groupId = "")
    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        ack.acknowledge();// 手动提交offset
    }

    /**
     * 监听多个 topic, 并指定分区和偏移
     * @param record
     * @param ack
     */
    @KafkaListener(groupId = "group-tim01", topicPartitions = {
            @TopicPartition(topic = "topic-tim01", partitions = {"0", "1"}),
            @TopicPartition(topic = "topic-tim02", partitions = {"0", "1"}, partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
    }, concurrency = "3")// concurrency 就是同组下的消费者个数,就是并发消息数, 建议 <= 分区数
    public void listenGroupPro(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        // 手动提交 offset
        ack.acknowledge();
    }
}
