package com.siemens.internship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ItemService {

    private static Logger log = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemRepository itemRepository;
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private volatile List<Item> processedItems = new ArrayList<>(); //have made the list volatile so that every change
    // that is being made will be flushed to the main memory
    private AtomicInteger processedCount = new AtomicInteger(0);//made the processedCount
    // Atomic for lock-free and thread-safe updates

    public List<Item> findAll() {
        log.info("ItemsService.findAll() called");
        List<Item> items = itemRepository.findAll();
        log.info("ItemsService.findAll() returned {} items", items.size());
        return items;
    }//end of findAll()

    public Optional<Item> findById(Long id) {
        if(id < 0){
            log.error("Invalid id");
            throw new IllegalArgumentException("Invalid id, the id must be a positive integer");
        }
        log.info("Find item by id: {}", id);
        return itemRepository.findById(id);
    }//end of findById(Long id)

    public Item save(Item item) {
        log.info("Save item: {}", item);
        return itemRepository.save(item);
    }//end of save(Item item)

    public void deleteById(Long id) {
        if(id < 0){
            log.error("Invalid id");
            throw new IllegalArgumentException("Invalid id, the id must be a positive integer");
        }
        log.info("Delete item by id: {}", id);
        itemRepository.deleteById(id);
    }//end of deleteById(Long id)


    /**TODO:hopefully checked
     * Your Tasks
     * Identify all concurrency and asynchronous programming issues in the code
     * Fix the implementation to ensure:
     * All items are properly processed before the CompletableFuture completes
     * Thread safety for all shared state
     * Proper error handling and propagation
     * Efficient use of system resources
     * Correct use of Spring's @Async annotation
     * Add appropriate comments explaining your changes and why they fix the issues
     * Write a brief explanation of what was wrong with the original implementation
     *
     * Hints
     * Consider how CompletableFuture composition can help coordinate multiple async operations
     * Think about appropriate thread-safe collections
     * Examine how errors are handled and propagated
     * Consider the interaction between Spring's @Async and CompletableFuture
     */
    @Async
    public List<Item> processItemsAsync() {

        log.info("ItemsService.processItemsAsync() called");
        List<Long> itemIds = itemRepository.findAllIds();

        for (Long id : itemIds) {
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(100);

                    Item item = itemRepository.findById(id).orElse(null);
                    synchronized (this) {//synchronized this to be entered by a single thread at once
                        if (item == null) {
                            return;
                        }
                    }

                    processedCount.incrementAndGet();
                    synchronized (this) {//synchronized this to be entered by a single thread at once or could have used a lock
                        item.setStatus("PROCESSED");
                        itemRepository.save(item);
                        processedItems.add(item);
                    }
                } catch (InterruptedException e) {
                    log.error("Interrupted while processing item {}", id, e);
                }
            }, executor);
        }
        return processedItems;
    }
}