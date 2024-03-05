package ru.wwerlosh.task.managment.search;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.task.managment.user.User;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping("/search")
    public Page<User> findUsersWithFilters(@RequestParam Integer page,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) String sortField,
                                           @RequestParam(required = false) String sortOrder,
                                           @RequestParam(required = false) String dateOfBirth,
                                           @RequestParam(required = false) String phone,
                                           @RequestParam(required = false) String fullName,
                                           @RequestParam(required = false) String email) {
        return searchService.findUsersWithFilters(page, size, sortField, sortOrder, dateOfBirth, phone, fullName, email);
    }
}
