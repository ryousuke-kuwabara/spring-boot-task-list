package dev.kuwa.tasklist.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.kuwa.tasklist.entity.Task;
import dev.kuwa.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String listTasks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<Task> tasks = taskService.findTasksByUsername(username);
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task());
        return "tasks";
    }

    @PostMapping
    public String addTask(@ModelAttribute Task newTask, @AuthenticationPrincipal UserDetails userDetails) {
        taskService.addTask(newTask, userDetails.getUsername());
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTaskCompletion(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.findTaskById(id);
        if (task != null && task.getUser().getUsername().equals(userDetails.getUsername())) {
            task.setCompleted(!task.isCompleted());
            taskService.updateTask(task);
        }
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.findTaskById(id);
        if (task != null && task.getUser().getUsername().equals(userDetails.getUsername())) {
            taskService.deleteTask(id);
        }
        return "redirect:/tasks";
    }
}
