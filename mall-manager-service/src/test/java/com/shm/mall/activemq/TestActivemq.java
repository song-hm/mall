package com.shm.mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActivemq {

	/**
	 * 点对点形式发送消息
	 * @throws Exception
	 */
	@Test
	public void testQueueProducer() throws Exception{
		//1、创建一个连接工厂对象。需要指定服务的IP和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、使用工厂对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用Connection对象的start方法
		connection.start();
		//4、创建一个Session对象
		//第一个参数是否开启使用，一般false不开启事务，如果true开启事务，则第二个参数无意义。
		//第二个参数：应答模式。自动应答和手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用Session对象创建一个Destination对象，两种方法queue和topic，现在使用queue
		Queue queue = session.createQueue("test-queue");
		//6、使用Session对象可创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7、创建一个Message对象，可以使用TextMessage
		/*TextMessage message = new ActiveMQTextMessage();
		message.setText("hello activemq!");*/
		TextMessage message = session.createTextMessage("hello activemq!");
		//8、发送消息
		producer.send(message);
		//9、关闭连接
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//1、创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//3、开启连接
		connection.start();
		//4、使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。queue对象
		Queue queue = session.createQueue("spring-queue");
		//5、使用Session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//6、接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//7、打印消息
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		//等待接收消息
		System.in.read();
		//8、关闭资源
		consumer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void testTopicProducer() throws Exception{
		//1、创建一个连接工厂对象。需要指定服务的IP和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、使用工厂对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用Connection对象的start方法
		connection.start();
		//4、创建一个Session对象
		//第一个参数是否开启使用，一般false不开启事务，如果true开启事务，则第二个参数无意义。
		//第二个参数：应答模式。自动应答和手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用Session对象创建一个Destination对象，两种方法queue和topic，现在使用topic 
		 Topic topic = session.createTopic("test-topic");
		//6、使用Session对象可创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		//7、创建一个Message对象，可以使用TextMessage
		/*TextMessage message = new ActiveMQTextMessage();
		message.setText("hello activemq!");*/
		TextMessage message = session.createTextMessage("top message");
		//8、发送消息
		producer.send(message);
		//9、关闭连接
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		//1、创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//3、开启连接
		connection.start();
		//4、使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。topic对象
		Topic topic = session.createTopic("test-topic");
		//5、使用Session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		//6、接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//7、打印消息
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		System.out.println("top的消费者3已经启动");
		//等待接收消息
		System.in.read();
		//8、关闭资源
		consumer.close();
		session.close();
		connection.close();
		
	}
}
