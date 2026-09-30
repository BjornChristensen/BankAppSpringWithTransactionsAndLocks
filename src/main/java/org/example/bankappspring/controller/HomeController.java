package org.example.bankappspring.controller;

import org.example.bankappspring.model.Account;
import org.example.bankappspring.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;

@Controller
public class HomeController {
  @Autowired
  AccountRepo accountRepo;

  @GetMapping("/")
  public String getHome(Model model){
    ArrayList<Account> accounts=accountRepo.getAllAccounts();
    model.addAttribute("accounts", accounts);
    return "home";
  }
}
