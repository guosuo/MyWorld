package com.exercise;

import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @Calssname EmailUtils1
 * @Description TODO
 * @Date 2022/6/29 11:08
 * @Create by 甲骨龙集成电脑
 */
public class EmailUtils1 {

  private static Logger log = (Logger) LoggerFactory.getLogger(EmailUtils1.class);
  public static final String SenderEmail = "897759517@qq.com";//发送人邮箱
  public static final String senderCode = "curlntdayqqubdag";//发送人邮箱授权码
  public static final String emailSMTPHost = "smtp.qq.com";//服务器地址

  public static final String receiveMailAccount = "897759517@qq.com";//收件人邮箱
  public static final String ccMailAccount = "guosuo234@163.com";//抄送人邮箱
  public static final String bccmailAccount = "983809937@qq.com";//密送人邮箱

  public static void sendMail() {

    try {
      Properties props = new Properties();
      props.setProperty("mail.transport.protocol", "smtp");// 使用的协议
      props.setProperty("mail.smtp.host", emailSMTPHost);// 发件人的邮箱的SMTP服务器地址
      props.setProperty("mail.smtp.auth", "true");// 需要请求认证

      Session session = Session.getInstance(props);//得到会话对象实例

      session.setDebug(false);//是否打印详细日志

      MimeMessage message = createMimeMessage(session);//获取邮件对象（封装了一个方法）

      Transport transport = session.getTransport();

      transport.connect(SenderEmail, senderCode);//连接发送人的邮箱账户

      // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
      transport.sendMessage(message, message.getAllRecipients());

      // 7. 关闭连接
      transport.close();

      log.info("邮件发送成功");
    } catch (Exception e) {
      log.info("发送邮件失败");
    }

  }


  public static MimeMessage createMimeMessage(Session session) throws Exception {
    // 1. 创建一封邮件
    MimeMessage message = new MimeMessage(session);

    // 2. From: 发件人
    message.setFrom(new InternetAddress(SenderEmail, "发件人", "UTF-8"));

    // 3. 设置收件人、抄送人、密送人
    //MimeMessage.RecipientType.TO：收件类型；MimeMessage.RecipientType.CC：抄送类型；MimeMessage.RecipientType.BCC：密送类型
    message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, "收件人", "UTF-8"));
    message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(ccMailAccount, "抄送人", "UTF-8"));
    message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(bccmailAccount, "密送人", "UTF-8"));

    // 4. Subject: 邮件主题
    message.setSubject("这是邮件的主题", "UTF-8");

    // 5. Content: 邮件正文（可以使用html标签）
    message.setContent("这是邮件正文", "text/html;charset=UTF-8");

    MimeMultipart multipart = new MimeMultipart();
    MimeBodyPart file1 = new MimeBodyPart();
    DataHandler handler = new DataHandler(new FileDataSource("D:\\工作\\需求\\货主\\department_info.sql"));
    file1.setDataHandler(handler);
    //对文件名进行编码，防止出现乱码
    String fileName = MimeUtility.encodeWord("文件名", "utf-8", "B");
    file1.setFileName(fileName);
    multipart.addBodyPart(file1);
    message.setContent(multipart);
    // 6. 设置发件时间
    message.setSentDate(new Date());
    // 7. 保存设置
    message.saveChanges();
    return message;
  }
}
