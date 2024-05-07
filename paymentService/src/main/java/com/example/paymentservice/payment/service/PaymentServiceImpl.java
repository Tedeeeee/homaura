package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.vo.Content;
import com.example.paymentservice.payment.Entity.Payment;
import com.example.paymentservice.payment.Entity.Status;
import com.example.paymentservice.payment.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RedisService redisService;
    private final RedissonClient redissonClient;

    private int uu;

    @Override
    public void itemCount(Content content, HttpServletRequest request) {
        RLock lock = redissonClient.getLock("stock");

        try {
            boolean available = lock.tryLock(20, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }

            String uuid = request.getHeader("uuid");
            String key = content.getProductUUID() + ":personal";

            long value = Long.parseLong(redisService.getValue(content.getProductUUID()));
            Long total = redisService.getTotal(key);

            if (value <= total) {
                throw new RuntimeException("재고가 부족합니다");
            }

            redisService.setValue(key, String.valueOf(uu++));

            Payment payment = Payment.builder()
                    .productUUID(content.getProductUUID())
                    .memberUUID(uuid)
                    .status(Status.PLUS)
                    .build();

            paymentRepository.save(payment);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void itemsCount(List<Content> content, HttpServletRequest request) {
        RLock lock = redissonClient.getLock("stock");

        try {
            boolean available = lock.tryLock(20, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }

            for (Content cont : content) {
                String uuid = request.getHeader("uuid");
                String key = cont.getProductUUID() + ":personal";

                long value = Long.parseLong(redisService.getValue(cont.getProductUUID()));
                Long total = redisService.getTotal(key);

                if (value <= total) {
                    throw new RuntimeException("재고가 부족합니다");
                }

                redisService.setValue(key, String.valueOf(uu++));

                Payment payment = Payment.builder()
                        .productUUID(cont.getProductUUID())
                        .memberUUID(uuid)
                        .status(Status.PLUS)
                        .build();

                paymentRepository.save(payment);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getStock(String productUUID) {
        String key = productUUID + ":personal";

        Long value = Long.parseLong(redisService.getValue(productUUID));
        Long total = redisService.getTotal(key);

        return (int) (value - total);
    }
}
