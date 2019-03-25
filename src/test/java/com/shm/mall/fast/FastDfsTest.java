package com.shm.mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.shm.mall.utils.FastDFSClient;

public class FastDfsTest {
	@Test
	public void TestUpload() throws Exception{
		//创建一个配置文件，文件名任意。内容就是tracker服务器地址。
		//使用全局对象加载配置文件。
		ClientGlobal.init("D:/Program Files/eclipse/workspace2/mall-manager-web/src/main/resources/conf/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackerClient对象获取一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StorageServer的引用，可以是null。
		StorageServer storageServer = null;
		//创建一个StorageClient,参数需要TrackerServer和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		//使用StorageClient上传文件
		String[] strings = storageClient.upload_file("D:\\kenan.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}


	@Test
	public void testFastDFSClient() throws Exception {
		FastDFSClient fastDFSClient = new FastDFSClient("D:/Program Files/eclipse/workspace2/mall-manager-web/src/main/resources/conf/client.conf");
		String string = fastDFSClient.uploadFile("D:\\kenan.jpg");
		System.out.println(string);
	}
}
