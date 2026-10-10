package com.gym.management.service;

import com.gym.management.entity.Payment;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

@Component
public class PaymentReceiptRenderer {
    public String render(Payment p) {
        String plan=p.getPlan()==null?"Outstanding fee":p.getPlan().getName();
        return "<!doctype html><html><head><meta charset='utf-8'><title>GymFlow Receipt</title><style>body{font:16px Arial;color:#18233b;max-width:620px;margin:50px auto;padding:24px;border:1px solid #ddd}h1{color:#5368df}small{color:#68748a}.row{padding:12px 0;border-bottom:1px solid #eee;display:flex;justify-content:space-between}.notice{margin-top:25px;padding:12px;background:#f1f3fa}@media print{button{display:none}body{margin:0;border:0}}</style></head><body><h1>GymFlow</h1><h2>Payment receipt</h2><div class='row'><span>Transaction</span><b>"+esc(p.getTransactionReference())+"</b></div><div class='row'><span>Member</span><b>"+esc(p.getMember().getFullName())+" ("+esc(p.getMember().getMemberCode())+")</b></div><div class='row'><span>Purpose</span><b>"+esc(p.getPurpose().name())+"</b></div><div class='row'><span>Plan</span><b>"+esc(plan)+"</b></div><div class='row'><span>Method</span><b>"+esc(p.getMethod().name())+"</b></div><div class='row'><span>Paid</span><b>"+formatInr(p.getAmount())+"</b></div><div class='row'><span>Completed</span><b>"+esc(String.valueOf(p.getCompletedAt()))+"</b></div><p class='notice'>Mock payment demonstration. No real money was processed.</p><button onclick='window.print()'>Print receipt</button><small><br>GymFlow — thank you.</small></body></html>";
    }
    private String formatInr(BigDecimal amount){NumberFormat f=NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));f.setCurrency(Currency.getInstance("INR"));f.setMinimumFractionDigits(2);f.setMaximumFractionDigits(2);return f.format(amount);}
    private String esc(String s){return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");}
}
