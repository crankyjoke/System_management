package com.example.demo.repository;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.example.demo.model.UserPosition;
public interface UserPositionRepository extends JpaRepository<UserPosition,Long>{
    @Override
    Optional<UserPosition> findById(Long id);
}
