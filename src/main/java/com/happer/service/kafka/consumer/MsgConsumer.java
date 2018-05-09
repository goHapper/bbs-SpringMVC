package com.happer.service.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.happer.po.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * �����ߣ�weikun��YST��
 * ���ڣ�2017/8/26
 * ˵˵���ܣ�
 */
@Component
public class MsgConsumer {

    //���¶�ȡ����������ֵ
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String servers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;


    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;


    @Value("${spring.kafka.consumer.auto-commit-interval-ms}")
    private String autoCommitInterval;


    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;


    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private String sessionTimeoutMs;


    private static KafkaConsumer<String, String> consumer;


    public KafkaConsumer<String, String> createConsumer() {

        if (consumer == null) {
            Properties props = new Properties();

            ////Kafka��Ⱥ���Ӵ��������ɶ��host:port���
            props.put("bootstrap.servers", this.servers);

            //Consumer��group id��ͬһ��group�µĶ��Consumer������ȡ���ظ�����Ϣ����ͬgroup�µ�Consumer��ᱣ֤��ȡ��ÿһ����Ϣ��ע�⣬ͬһ��group�µ�consumer�������ܳ���������������Ҫʹ�ñ�������ƣ� ��������ߺ������߶���ͬһ�飬���ܷ���ͬһ���ڵ�topic����
            props.put("group.id", this.groupId);
            //�Ƿ��Զ��������ύ�Ѿ���ȡ�����Ѷ˵���Ϣoffset ���յ� �϶��ܽ��յ����������������false
            //��ô��ͱ����ֶ��ύ�������������ǰû���ѵ�����һ��ȡ����������Զ��ύ��֮�󽫲��ٿ����ظ�����

            //�Ƿ��Զ��ύ����ȡ��Ϣ��offset���ύoffset����Ϊ����Ϣ�Ѿ��ɹ������ѣ�
            // �����µ�Consumer�޷�����ȡ������Ϣ�������ֶ��޸�offset����Ĭ��Ϊtrue
            props.put("enable.auto.commit", this.enableAutoCommit);
            //ԭ����Kafka�µ������ߣ�Ĭ������»�����һ�����ѽ������ѣ����ǿ�ʼ���ѵ�ʱ��
            // ��������ӵ���Ϣ��ʼ���������ҿ�ʼ��ӵ�1000���Ժ󣬲ŻῪʼ����
            //���Ա���Ҫ����auto.offset.reset�����¼���������ߣ���ͷ����ʼ�������ѡ���Ȼ��Щ�����
            // ������Ҫ�����µĿ�ʼ����
            props.put("auto.offset.reset", this.autoOffsetReset);
            //�Զ��ύoffset�ļ����������Ĭ��5000��
            //�� ���в��õ����Զ��ύoffset��Kafka client������һ���̶߳��ڽ�offset�ύ��broker���������Զ��ύ�ļ���ڷ������ϣ���������JVM��������������ô��һ������Ϣ�ǻᱻ �ظ����ѵġ�Ҫ������һ���⣬��ʹ���ֶ��ύoffset�ķ�ʽ������consumerʱ��enable.auto.commit��Ϊfalse�����ڴ� ������consumer.commitSync()���ֶ��ύ��
            props.put("auto.commit.interval.ms", this.autoCommitInterval);

            props.put("session.timeout.ms", this.sessionTimeoutMs);
            props.put("key.deserializer", this.keyDeserializer);
            props.put("value.deserializer", this.valueDeserializer);

            consumer = new KafkaConsumer<String, String>(props);
        }
        return consumer;
    }
    private ExecutorService executorPool;
    public synchronized List<Message> consumerMsg(Integer uid) {//uid��ǰ�û�id

        KafkaConsumer<String, String> myconsumer= this.createConsumer() ;

        myconsumer.subscribe(Arrays.asList("reply"+uid));//�����˻�������

        List<Message> result = new ArrayList<>();

        synchronized (this){
            ConsumerRecords<String, String> records=myconsumer.poll(100);

            for(ConsumerRecord<String, String> record:records){
                System.out.println("�յ�������-��������������������������fetched from partition " + record.partition() + ", offset: " + record.offset() + ",key:" + record.key() + ", message: " + record.value());
                Message m=JSON.parseObject(record.value(),Message.class);
                result.add(m);
            }

            myconsumer.commitSync();
        }

        return result;
    }



}
