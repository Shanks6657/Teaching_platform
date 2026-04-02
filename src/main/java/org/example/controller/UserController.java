package org.example.controller;

import org.example.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class UserController {

    // Simple in-memory user store for demonstration
    private final Map<String, User> userDatabase = new HashMap<>();
    private static final Set<String> ALLOWED_ROLES = new HashSet<>(Arrays.asList("student", "teacher"));

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               Model model) {
        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedRole = role == null ? "" : role.trim().toLowerCase();

        if (normalizedUsername.isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "用户名和密码不能为空！");
            model.addAttribute("username", normalizedUsername);
            model.addAttribute("role", normalizedRole);
            return "register";
        }
        if (normalizedUsername.length() < 3 || normalizedUsername.length() > 20) {
            model.addAttribute("error", "用户名长度需在 3 到 20 个字符之间！");
            model.addAttribute("username", normalizedUsername);
            model.addAttribute("role", normalizedRole);
            return "register";
        }
        if (password.length() < 6 || password.length() > 32) {
            model.addAttribute("error", "密码长度需在 6 到 32 个字符之间！");
            model.addAttribute("username", normalizedUsername);
            model.addAttribute("role", normalizedRole);
            return "register";
        }
        if (!ALLOWED_ROLES.contains(normalizedRole)) {
            model.addAttribute("error", "身份选择无效，请重新选择！");
            model.addAttribute("username", normalizedUsername);
            return "register";
        }
        if (userDatabase.containsKey(normalizedUsername)) {
            model.addAttribute("error", "用户名已存在！");
            model.addAttribute("username", normalizedUsername);
            model.addAttribute("role", normalizedRole);
            return "register";
        }

        userDatabase.put(normalizedUsername, new User(normalizedUsername, password, normalizedRole));
        return "redirect:/login?registered=1";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "registered", required = false) String registered,
                                Model model) {
        if ("1".equals(registered)) {
            model.addAttribute("success", "注册成功，请使用账号登录。");
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        String normalizedUsername = username == null ? "" : username.trim();
        if (normalizedUsername.isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "请输入用户名和密码！");
            return "login";
        }

        User user = userDatabase.get(normalizedUsername);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("currentUser", user);
            return "redirect:/home";
        }
        model.addAttribute("error", "用户名或密码错误！");
        return "login";
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        
        if ("student".equals(currentUser.getRole())) {
            model.addAttribute("courses", Arrays.asList("高等数学 - 已选修", "大学物理 - 已选修", "计算机科学 - 已选修"));
        } else if ("teacher".equals(currentUser.getRole())) {
            model.addAttribute("courses", Arrays.asList("数据结构 (2026春)", "计算机网络 (2026秋)"));
        }
        
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
