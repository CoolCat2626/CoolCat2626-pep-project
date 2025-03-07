package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    
    @Override
    public Message createMessage(Message message) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                message.setMessage_id(generatedId);
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
    
    @Override
    public Message getMessageById(int messageId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public Message deleteMessageById(int messageId) {
        Message message = getMessageById(messageId);
        
        if (message != null) {
            try (Connection conn = ConnectionUtil.getConnection()) {
                String sql = "DELETE FROM Message WHERE message_id = ?";
                
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, messageId);
                
                ps.executeUpdate();
                return message;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    @Override
    public Message updateMessageById(int messageId, String messageText) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, messageText);
            ps.setInt(2, messageId);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Message> getMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
}