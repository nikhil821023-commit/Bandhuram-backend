// service/OrderService.java
package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.*;
import com.bandhuram.backend.entity.*;
import com.bandhuram.backend.exception.ResourceNotFoundException;
import com.bandhuram.backend.repository.MenuItemRepository;
import com.bandhuram.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public OrderResponse placeOrder(OrderRequest req) {
        Order order = Order.builder()
                .customerName(req.customerName().trim())
                .phone(req.phone().trim())
                .notes(req.notes())
                .build();

        for (OrderItemRequest itemReq : req.items()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.menuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + itemReq.menuItemId()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItemId(menuItem.getId())
                    .itemName(menuItem.getName())
                    .priceLabel(menuItem.getPriceLabel())
                    .quantity(itemReq.quantity())
                    .build();

            order.getItems().add(orderItem);
        }

        Order saved = orderRepository.save(order);
        OrderResponse dto = toDto(saved);

        // push to any admin currently connected
        messagingTemplate.convertAndSend("/topic/orders", dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toDto).toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        order.setStatus(status);
        return toDto(order);
    }

    private OrderResponse toDto(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getMenuItemId(), i.getItemName(), i.getPriceLabel(), i.getQuantity()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getPhone(),
                order.getNotes(),
                order.getStatus(),
                order.getCreatedAt(),   // Instant — 6th position
                items                   // List<OrderItemResponse> — 7th position
        );
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found: " + id);
        }
        orderRepository.deleteById(id);
    }


}