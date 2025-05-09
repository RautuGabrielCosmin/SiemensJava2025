package com.siemens.internship;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    //added a logger for tracing and debugging
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        log.info("GET /api/items - Fetching all items");
        List<Item> items = itemService.findAll();//a list that contains all the items
        log.info("GET /api/items - Returning {} items", items.size());//logging hw many items were returned
        return ResponseEntity.ok(items);
    }//end of getAllItems()

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item, BindingResult result) {
        log.info("POST /api/items - Creating new item: {}", item);
        if (result.hasErrors()) {
            log.warn("POST /api/items - Validation failed: {}", result.getFieldErrors());
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
        log.info("POST /api/items - Validation passed, saving item");
        return new ResponseEntity<>(itemService.save(item), HttpStatus.BAD_REQUEST);
    }//end of createItem(@Valid @RequestBody Item item, BindingResult result)

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        log.info("GET /api/items/{} - Retrieving item by ID", id);
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            log.info("GET /api/items/{} - Retrieving item by ID", id);
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        }
        else{
            log.warn("GET /api/items/{} - Item not found", id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }//end of getItemById(@PathVariable Long id)

    @PutMapping("/{id}")
    //have added @Valid and the BindingResult result to hold the validation errors from the @Valid annotation
    public ResponseEntity<Item> updateItem(@PathVariable Long id,@Valid @RequestBody Item item, BindingResult result) {
        log.info("PUT /api/items/{} - Updating item with data: {}", id, item);
        if (result.hasErrors()) {
            log.warn("PUT /api/items/{} - Validation failed: {}", id, result.getFieldErrors());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Optional<Item> existingItem = itemService.findById(id);
        if (existingItem.isPresent()) {
            log.info("PUT /api/items/{} - Updating item with data: {}", id, item);
            item.setId(id);
            return new ResponseEntity<>(itemService.save(item), HttpStatus.CREATED);
        } else {
            log.warn("PUT /api/items/{} - Item not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);//changed from NO_CONTENT to NOT_FOUND for error404
        }
    }//end of updateItem(@PathVariable Long id,@Valid @RequestBody Item item, BindingResult result)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.info("DELETE /api/items/{} - Deleting item", id);
        itemService.deleteById(id);
        log.info("DELETE /api/items/{} - Deleted request processed", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);//changed from CONFLICT to NOT_FOUND for error404
    }//end of deleteItem(@PathVariable Long id)

    @GetMapping("/process")
    public ResponseEntity<List<Item>> processItems() {
        log.info("GET /api/items/process - Initiating asynchronous processing");
        List<Item> processed = itemService.processItemsAsync();
        log.info("GET /api/items/process - Completed processing {} items processed", processed.size());
        return new ResponseEntity<>(processed, HttpStatus.OK);
    }//end of processItems()
}
