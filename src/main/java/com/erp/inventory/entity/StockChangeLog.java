package com.erp.inventory.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "stock_change_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockChangeLog {
    @Id
    private String id;
    private String productId;
    private String userId;
    private String warehouseId;
    private int changeAmount;
    private String comment;
    private LocalDateTime timestamp;
}