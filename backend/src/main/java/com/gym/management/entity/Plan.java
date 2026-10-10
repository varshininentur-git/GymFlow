package com.gym.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "membership_plans")
public class Plan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(nullable = false, unique = true) private String name;
    @NotNull @Min(1) @Max(365) @Column(nullable = false) private Integer durationDays;
    @NotNull @DecimalMin("0.01") @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(length = 500) private String description;
    @Column(nullable = false) private boolean active = true;

    protected Plan() {}
    public Plan(String name, Integer durationDays, BigDecimal price, String description) {
        this.name = name; this.durationDays = durationDays; this.price = price; this.description = description;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
