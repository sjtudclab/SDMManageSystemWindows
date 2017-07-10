package org.dclab.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.dclab.model.Model;
import org.dclab.model.User;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZooKeeperService {
	public static int CLIENT_PORT = 2181;
	public static int TIME_OUT = 2000;
	//public static int flag = 1;
	// public static String IP="192.168.0.184:";
	public static String IP = "localhost:";

	public int zookeeperWatch(final Model model, final List<User> users) throws Exception {

		ZooKeeper zk = new ZooKeeper(IP + CLIENT_PORT, TIME_OUT, new Watcher() {
			// 监控所有被触发的事件
			public void process(WatchedEvent event) {
				System.out.println("已经触发了" + event.getType() + "事件！");
				if (users != null) { //用户列表不为空，则调用EmailService发送邮件
					EmailService email = new EmailService();
					try {
						email.sendEmail(users, model);//发送邮件
						//flag = 1;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		// 创建一个目录节点
		// zk.create("/testRootPath", "testRootData".getBytes(),
		// Ids.OPEN_ACL_UNSAFE,
		// CreateMode.PERSISTENT);
		String url = "/SDM/" + model.getBigClass() + "/" + model.getMiddleClass() + "/" + model.getSmallClass();
		// 创建一个子目录节点
		zk.create(url+"/"+ model.getEnglishName(), model.getEnglishName().getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.getChildren(url, true);
		//zk.exists(url, true);
		// System.out.println(new
		// String(zk.getData("/testRootPath",false,null)));
	    // 关闭连接
	    zk.close();
		return model.getElementID();
	}

	public ZooKeeperService() {
		// TODO Auto-generated constructor stub
	}
}
