package userone;

import userone.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    @Autowired NotificationRepository notificationRepository;
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBookingCreated_SendNotification(@Payload BookingCreated bookingCreated){

        if(bookingCreated.isMe()){
            // 노티 내용 SET
            Notification notification = new Notification();
            notification.setUserId(bookingCreated.getBookingUserId());
            notification.setContents("conference room[" + bookingCreated.getRoomId() + "] reservation is complete");
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            notification.setSendDtm(nowDate);
            notificationRepository.save(notification);
            System.out.println("##### listener SendNotification : " + bookingCreated.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverUseStarted_SendNotification(@Payload UseStarted useStarted) {
        if(useStarted.isMe()) {
            Notification notification = new Notification();
            notification.setUserId("99999");
            notification.setContents("Booking Number[ " + String.valueOf(useStarted.getBookingId()) + " ] is use started");
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            notification.setSendDtm(nowDate);
            notificationRepository.save(notification);
            System.out.println("##### listener SendNotification : " + useStarted.toJson());
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBookingCancelled_SendNotification(@Payload BookingCancelled bookingCancelled){

        if(bookingCancelled.isMe()){
            Notification notification = new Notification();
            notification.setUserId(bookingCancelled.getBookingUserId());
            notification.setContents("reservation has been canceled");
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            notification.setSendDtm(nowDate);
            notificationRepository.save(notification);
            System.out.println("##### listener SendNotification : " + bookingCancelled.toJson());
        }
    }

}
