package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    
    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || 
            message.getMessage_text().isBlank() || 
            message.getMessage_text().length() > 255) {
            return null;
        }
        
        if (accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
        
        return messageDAO.createMessage(message);
    }
    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
    
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }
    
    public Message updateMessageById(int messageId, String messageText) {
        Message existingMessage = messageDAO.getMessageById(messageId);

        if (existingMessage == null) {
            return null;
        }
        
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            return null;
        }
        
        return messageDAO.updateMessageById(messageId, messageText);
    }
    
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}