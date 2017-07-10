package org.dclab.zk;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.dclab.model.Model;
import org.dclab.model.User;

public class EmailService {
	// 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
	// 126邮箱给 SMTP 客户端设置了“授权码”, 这里的邮箱密码必需使用这个授权码
	public static String myEmailAccount = "jinyueyuan@126.com";
	public static String myEmailPassword = "yue123456";

	// 发件人邮箱的 SMTP 服务器地址, 必须准确, 网易126邮箱的 SMTP 服务器地址为: smtp.126.com
	public static String myEmailSMTPHost = "smtp.126.com";

	// // 收件人邮箱（为有效邮箱）
	// public static String receiveMailAccount = "757543865@qq.com";

	public void sendEmail(List<User> users, Model model) throws Exception {

		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", myEmailSMTPHost); // 发件人的邮箱的 SMTP服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

		// PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
		// 如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
		// 打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
		/*
		 * // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接, //
		 * 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助, // QQ邮箱的SMTP(SLL)端口为465或587,
		 * 其他邮箱自行去查看) final String smtpPort = "465";
		 * props.setProperty("mail.smtp.port", smtpPort);
		 * props.setProperty("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * props.setProperty("mail.smtp.socketFactory.fallback", "false");
		 * props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		 */
		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props); // 根据参数配置，创建会话对象（为了发送邮件准备的）
		session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log
		// 3. 创建一封邮件
		MimeMessage message = createMimeMessage(session, myEmailAccount, users, model);
		if (message != null) {
			// 4. 根据 Session 获取邮件传输对象
			Transport transport = session.getTransport();

			// 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
			//
			// PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
			// 仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
			// 类型到对应邮件服务器的帮助网站上查看具体失败原因。
			//
			// PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
			// (1) 邮箱没有开启 SMTP 服务;
			// (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
			// (3) 邮箱服务器要求必须要使用 SSL 安全连接;
			// (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
			// (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
			//
			// PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
			transport.connect(myEmailAccount, myEmailPassword);

			// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients()
			// 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
			transport.sendMessage(message, message.getAllRecipients());

			// 7. 关闭连接
			transport.close();
		}
	}

	// /**
	// * 创建一封只包含文本的简单邮件
	// *
	// * @param session 和服务器交互的会话
	// * @param sendMail 发件人邮箱
	// * @param receiveMail 收件人邮箱
	// * @return
	// * @throws Exception
	// */
	public MimeMessage createMimeMessage(Session session, String sendMail, List<User> users, Model model)
			throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);
		// 2. From: 发件人
		// 其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
		message.setFrom(new InternetAddress(sendMail, "共享数据模型管理系统", "UTF-8"));

		// 3. To: 收件人
		message.setRecipient(MimeMessage.RecipientType.TO,
				new InternetAddress(users.get(0).getEmail(), users.get(0).getUsername(), "UTF-8"));
		for (int i = 1; i < users.size(); i++) {
			// To: 增加收件人（可选）
			message.addRecipient(MimeMessage.RecipientType.TO,
					new InternetAddress(users.get(i).getEmail(), users.get(i).getUsername(), "UTF-8"));
		}
		String title = model.getCreator() + "于" + model.getCreateTime() + "创建元素：" + model.getEnglishName() + ",中文名称"
				+ model.getChineseName();
		// 4. Subject: 邮件主题
		message.setSubject(title, "UTF-8");
		// "元素ID："+model.getElementID()+"\n"+
		String content = "元素英文名称：" + model.getEnglishName() + "\n" + "元素中文名称：" + model.getChineseName() + "\n" + "创建者："
				+ model.getCreator() + "\n" + "创建时间：" + model.getCreateTime() + "\n" + "类别：" + model.getBigClass() + "/"
				+ model.getMiddleClass() + "/" + model.getSmallClass() + "\n" + "请您于3天内登录"
				+ "http://localhost:8080/SDMManageSystem/tpls/models/checkCR_CCB.html" + "  进行审核投票。";
		// 5. Content: 邮件正文（可以使用html标签）
		message.setContent(content, "text/html;charset=UTF-8");

		// 6. 设置显示的发件时间
		message.setSentDate(new Date());

		// 7. 保存前面的设置
		message.saveChanges();

		return message;
	}

	public EmailService() {
		// TODO Auto-generated constructor stub
	}

}
