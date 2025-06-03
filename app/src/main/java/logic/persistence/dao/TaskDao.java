package logic.persistence.dao;

import logic.general.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private final Connection connection;

    public TaskDao(Connection connection) {
        this.connection = connection;
    }

    public void addTask(Task task) {
        try {
            String sql = "INSERT INTO task (description, transcript_id, assignee_id) values(?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, task.getDescription());
                stmt.setInt(2, task.getTranscriptId());
                stmt.setInt(3, task.getAssigneeId());
                stmt.execute();
            }

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid();")) {
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task) {
        try {
            String sql = "DELETE FROM task WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, task.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTasksByTranscript(int transcriptId) {
        try {
            String sql = "DELETE FROM task WHERE transcript_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, transcriptId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task) {
        try {
            String sql = "UPDATE task SET description = ?, transcript_id = ?, assignee_id = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, task.getDescription());
                stmt.setInt(2, task.getTranscriptId());
                stmt.setInt(3, task.getAssigneeId());
                stmt.setInt(4, task.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasksByTranscriptId(int transcriptId) {
        List<Task> tasks = new ArrayList<>();
        try {
            String sql = "SELECT * FROM task WHERE transcript_id = ? ORDER BY transcript_id";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, transcriptId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Task task = new Task(rs.getInt("transcript_id"), rs.getInt("assignee_id"), rs.getString("description"));
                        task.setId(rs.getInt("id"));
                        tasks.add(task);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public Task getTaskById(int taskId) {
        try {
            String sql = "SELECT * FROM task WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, taskId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Task task = new Task(rs.getInt("transcript_id"), rs.getInt("assignee_id"), rs.getString("description"));
                        task.setId(rs.getInt("id"));
                        return task;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
