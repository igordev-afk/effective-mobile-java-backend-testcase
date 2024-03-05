package ru.wwerlosh.task.managment.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wwerlosh.task.managment.user.User;
import ru.wwerlosh.task.managment.user.UserRepository;

@Service
public class SearchService {

    private final UserRepository userRepository;

    public SearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<User> findUsersWithFilters(Integer page, Integer size, String sortField, String sortOrder,
                                           String dateOfBirth, String phone, String fullName, String email) {

        if (size == null) {
            size = 10;
        }
        if (sortField == null) {
            sortField = "firstName";
        }
        if (sortOrder == null) {
            sortOrder = "asc";
        }

        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findByFilters(dateOfBirth, phone, fullName, email, pageable);
    }
}
