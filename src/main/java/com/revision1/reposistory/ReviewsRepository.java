package com.revision1.reposistory;

import com.revision1.enity.Property;
import com.revision1.enity.Reviews;
import com.revision1.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {

    List<Reviews> findAllByUser(User user);

    Reviews findByPropertyAndUser(Property property, User user);


}