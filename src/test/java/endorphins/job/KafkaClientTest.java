package endorphins.job;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author timothy
 * @createTime 2022年01月19日 22:17:00
 */
public class KafkaClientTest {

    private static final String TOPIC_NAME = "topic-tim01";

    @Test
    public void kafka_producer_client_test() throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        // 配置服务器
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.20:9092,192.168.1.21:9092");
        // 把发送的key 从字符串序列化为字节数组
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 把发送的消息value 从字符串序列化为字节数组
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 生产者
        Producer<String, String> producer = new KafkaProducer<>(properties);

        /**
         * key 通常是指定在那个分区
         * 未指定发送分区，计算方式 hash(key)%partitionNum
         */
        ProducerRecord<String, String> producerRecord = new ProducerRecord(TOPIC_NAME, "1", "TEST value");

        // 同步发送
//        RecordMetadata recordMetadata = producer.send(producerRecord).get();
//        System.out.println("同步方式发送消息结果："
//                + "topic" + recordMetadata.topic()
//                + ",partition-" + recordMetadata.partition()
//                + ", offset-" + recordMetadata.offset());
        // 异步发送

        CountDownLatch countDownLatch = new CountDownLatch(5);// 设置回调次数
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                countDownLatch.countDown(); // 回调一次 + 1
                System.out.println("回调次数" + (5 - countDownLatch.getCount()));
            }
        });
        countDownLatch.await(3, TimeUnit.SECONDS); // 3秒查看一次，count 为 0 时不再阻塞
        producer.close();
    }

    @Test
    public void kafka_consumer_client_test() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.20:9092,192.168.1.21:9092,192.168.1.21:9092");
        // 把发送的key 从字符串序列化为字节数组
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 把发送的消息value 从字符串序列化为字节数组
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("收到消息 = %d,offset = %d,key = %s, value %s %n",
                        record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

    public Consumer getConsumerInterval(Properties properties, String topicName, long startTime) {
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
        List<PartitionInfo> topicPartitions = consumer.partitionsFor(topicName);
        Map<TopicPartition, Long> map = new HashMap<>();
        for (PartitionInfo par : topicPartitions) {
            map.put(new TopicPartition(topicName, par.partition()), startTime);
        }
        // 根据时间查询 offset
        Map<TopicPartition, OffsetAndTimestamp> parMap = consumer.offsetsForTimes(map);
        parMap.forEach(
                (topicPartition, offsetAndTimestamp) -> {
                    if (topicPartition == null || offsetAndTimestamp == null) return;
                    long offset = offsetAndTimestamp.offset();
                    consumer.assign(Arrays.asList(topicPartition));
                    consumer.seek(topicPartition,offset);
                }
        );
        consumer.commitAsync();
        return consumer;
    }
}
