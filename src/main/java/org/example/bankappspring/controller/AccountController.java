package org.example.bankappspring.controller;

import org.example.bankappspring.model.Account;
import org.example.bankappspring.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {
  @Autowired
  AccountRepo accountRepo;

  @GetMapping("/deposit")
  public String getDeposit(
    @RequestParam("accountNo") int accountNo, Model model) {
    Account account=accountRepo.getAccount(accountNo);
    model.addAttribute("account", account);
    return "deposit";
  }

  @PostMapping("/deposit")
  public String deposit(
    @RequestParam("accountNo") int accountNo,
    @RequestParam double amount) {
    accountRepo.deposit(accountNo, amount);
    return "redirect:/";
  }

  @GetMapping("/withdraw")
  public String showWithdraw(
    @RequestParam("accountNo") int accountNo, Model model) {
    Account account = accountRepo.getAccount(accountNo);
    model.addAttribute("account", account);
    return "withdraw";
  }

  @PostMapping("/withdraw")
  public String withdraw(
    @RequestParam("accountNo") int accountNo,
    @RequestParam("balance") double balance,
    @RequestParam("amount") double amount,
    Model model) {
    try {
      accountRepo.withdraw(accountNo, amount);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/transfer")
  public String showTransfer(
    @RequestParam("fromAccountNo") int fromAccountNo,
    @RequestParam("toAccountNo") int toAccountNo,
    Model model) {
    Account fromAccount = accountRepo.getAccount(fromAccountNo);
    Account toAccount = accountRepo.getAccount(toAccountNo);
    model.addAttribute("fromAccount", fromAccount);
    model.addAttribute("toAccount", toAccount);
    return "transfer";
  }

  @PostMapping("/transfer")
  public String transfer(
    @RequestParam("fromAccountNo") int fromAccountNo,
    @RequestParam("toAccountNo") int toAccountNo,
    @RequestParam("amount") double amount,
    Model model) {
    try {
      accountRepo.transfer(fromAccountNo, toAccountNo, amount);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      return "error";
    }
  }
}