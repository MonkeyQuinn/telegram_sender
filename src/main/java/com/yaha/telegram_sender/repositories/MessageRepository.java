package com.yaha.telegram_sender.repositories;

import com.yaha.telegram_sender.model.entities.AppUser;
import com.yaha.telegram_sender.model.entities.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<UserMessage, Long> {

    List<UserMessage> findAllByAuthor(AppUser author);

}
