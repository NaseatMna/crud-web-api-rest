//package com.nanita.helpers.schedule;
//
//import com.nanita.controller.printinvoice.HtmlConverterController;
//import com.nanita.html2pdf.HtmlConverterUtil;
//import com.nanita.model.Mail;
//import com.nanita.model.invoice.InvoiceSchedule;
//import com.nanita.service.MailSenderService;
//import com.nanita.service.MailService;
//import com.nanita.service.invoice.InvoiceScheduleService;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Property;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.mail.MessagingException;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import static com.nanita.constant.CoreConstants.GENERATE_INVOICE_SCHEDULE;
//import static com.nanita.constant.CoreConstants.INVOICE_GENERATE_TO_PDF;
//import static com.nanita.constant.CoreConstants.SEND_MAIL_SCHEDULE;
//
///**
// * Created by sophatvathana on 5/24/17.
// */
//@Component
//public class ScheduledTasks {
//	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
//	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	@Autowired
//	private MailService mailService;
//
//	@Autowired
//	private MailSenderService mailSenderService;
//
//	@Autowired
//	private InvoiceScheduleService invoiceScheduleService;
//
//	@Scheduled(cron = SEND_MAIL_SCHEDULE)
//	public void reportCurrentTime() throws MessagingException, IOException {
//		Mail mail;
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Mail.class);
//		detachedCriteria.add(Property.forName("type").eq(com.nanita.model.Type.Mail.NEWSLETTER.getName()));
//		detachedCriteria.addOrder(Order.desc("createdAt"));
//		mail = mailService.get(detachedCriteria);
//		if (mail != null) {
//			mailSenderService.sendVerifyEmail(mail,
//					mailSenderService.getEditableMailTemplate(),
//					Locale.US);
//			mailService.delete(mail.getId());
//			log.info("The time is now {}", dateFormat.format(new Date()));
//		}
//	}
//
//	@Scheduled(fixedDelay = 5000)
//	private void generatePdf() throws IOException, InterruptedException {
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(InvoiceSchedule.class);
//		detachedCriteria.addOrder(Order.desc("createdAt"));
//		InvoiceSchedule invoiceSchedule = invoiceScheduleService.get(detachedCriteria);
//		if (invoiceSchedule != null) {
//			System.out.println("strings context =========================> " + invoiceSchedule.getContent());
//
//			String formattedString = invoiceSchedule.getContent()
//					.replace(",", "")  //remove the commas
//					.replace("[", "")  //remove the right bracket
//					.replace("]", "")  //remove the left bracket
//					.trim();
//
//			String[] strings = formattedString.split(" ");
//
//			HtmlConverterController.pdf(strings, invoiceSchedule.getRequestPath(), invoiceSchedule.getBaseUrl());
//
//			for (String invoiceNo: strings) {
//				System.out.println("output a ======================= " + invoiceNo);
//			}
//
//			invoiceScheduleService.delete(invoiceSchedule.getId());
//		}
//	}
//}