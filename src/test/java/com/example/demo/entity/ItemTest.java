package com.example.demo.entity;

import com.example.demo.config.QueryDslConfig;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class ItemTest {

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createItemTest() {

        User owner = new User("user", "qwer@naver.com", "OwnerNickName", "test1234");
        User manager = new User("user", "qwermanager.naver.com", "ManagerNickname", "test1234");

        userRepository.save(owner);
        userRepository.save(manager);

        Item item = new Item("ItemName", "ItemDescription", manager, owner);

        assertThrows(ConstraintViolationException.class, () -> itemRepository.saveAndFlush(item),
                "status 값이 null로 지정되는 Item은 ConstraintViolationException을 발생시킨다.");

//        assertThrows(PersistenceException.class, () -> {
//            em.createNativeQuery(
//                    "INSERT INTO item(name, description, owner_id, manager_id, status) VALUES ('ItemName', 'ItemDescription', 1, 2, NULL)"
//            ).executeUpdate();
//        });
    }
}
