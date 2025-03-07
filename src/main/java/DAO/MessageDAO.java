package DAO;

import Model.Message;
import java.util.List;

public interface MessageDAO {
    Message createMessage(Message message);
    List<Message> getAllMessages();
    Message getMessageById(int messageId);
    Message deleteMessageById(int messageId);
    Message updateMessageById(int messageId, String messageText);
    List<Message> getMessagesByAccountId(int accountId);
}
