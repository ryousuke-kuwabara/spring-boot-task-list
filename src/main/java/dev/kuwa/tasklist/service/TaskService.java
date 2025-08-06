package dev.kuwa.tasklist.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.kuwa.tasklist.entity.Task;
import dev.kuwa.tasklist.entity.User;
import dev.kuwa.tasklist.repository.TaskRepository;
import dev.kuwa.tasklist.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> findTaskByUsername(String username) {
        User user = findUserByUsername(username);
        return taskRepository.findByUserId(user.getId());
    }

    public void addTask(Task task, String username) {
        User user = findUserByUsername(username);
        task.setUser(user);
        taskRepository.save(task);
    }

    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません。"));
    }
}
