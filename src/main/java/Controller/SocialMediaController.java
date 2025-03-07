package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import DAO.MessageDAO;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    ObjectMapper mapper;
    
    public SocialMediaController() {
        AccountDAO accountDAO = new AccountDAOImpl();
        MessageDAO messageDAO = new MessageDAOImpl();
        
        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO, accountDAO);
        this.mapper = new ObjectMapper();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // Account endpoints
        app.post("/register", this::registerAccount);
        app.post("/login", this::login);
        
        // Message endpoints
        app.post("/messages", this::createMessage);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/messages", this::getAllMessages);
        app.patch("/messages/{message_id}", this::updateMessageById);
        
        return app;
    }

    private void registerAccount(Context context) {
        try {
            Account account = mapper.readValue(context.body(), Account.class);
            Account registeredAccount = accountService.registerAccount(account);
            
            if (registeredAccount != null) {
                context.json(registeredAccount);
            } else {
                context.status(400);
            }
        } catch (Exception e) {
            context.status(400);
        }
    }
    
    private void login(Context context) {
        try {
            Account account = mapper.readValue(context.body(), Account.class);
            Account loggedInAccount = accountService.login(account);
            
            if (loggedInAccount != null) {
                context.json(loggedInAccount);
            } else {
                context.status(401);
            }
        } catch (Exception e) {
            context.status(401);
        }
    }
    
    private void createMessage(Context context) {
        try {
            Message message = mapper.readValue(context.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            
            if (createdMessage != null) {
                context.json(createdMessage);
            } 
            else {
                context.status(400);
            }
        } 
        catch (Exception e) {
            context.status(400);
        }
    }
    
    private void deleteMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
        
            Message deletedMessage = messageService.deleteMessageById(messageId);
        
            if (deletedMessage != null) {
                context.json(deletedMessage);
            } 
            else {
                context.json("");
            }
        }   
        catch (Exception e) {
            context.status(200);
            context.json("");
        }
    }

    private void getMessagesByAccountId(Context context) {
        try {
            int accountId = Integer.parseInt(context.pathParam("account_id"));
        
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            context.json(messages);
            } 
        catch (Exception e) {
            context.json(new ArrayList<Message>());
        }
    }

    private void getAllMessages(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
            context.json(messages);
        } 
        catch (Exception e) {
            context.json(new ArrayList<Message>());
        }
    }

    private void getMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
        
            Message message = messageService.getMessageById(messageId);
        
            if (message != null) {
                    context.json(message);
            } 
            else {
                context.json("");
            }
        } 
        catch (Exception e) {
            context.status(200);
            context.json("");
        }
    }

    private void updateMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
        
            Message messageUpdate = mapper.readValue(context.body(), Message.class);
            String newMessageText = messageUpdate.getMessage_text();
            Message updatedMessage = messageService.updateMessageById(messageId, newMessageText);
        
            if (updatedMessage != null) {
                context.json(updatedMessage);
            } 
            else {
                context.status(400);
            }
        } 
        catch (Exception e) {
            context.status(400);
        }
    }
}