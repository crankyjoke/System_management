package com.example.demo.Service;
import com.example.demo.model.UserPosition;
import com.example.demo.model.User;
import com.example.demo.repository.UserPositionRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import com.example.demo.Service.OrganizationTableService;
@Service
public class UserPositionService {
    private final UserPositionRepository userPositionRepository;
    private final OrganizationTableService organizationTableService;
    public UserPositionService(UserPositionRepository userPositionRepository, OrganizationTableService organizationTableService){
        this.userPositionRepository = userPositionRepository;
        this.organizationTableService = organizationTableService;
    }
//    Long id, List<String> positionName, Long balance, String organization
    public UserPosition createUserPosition(Long id){
        UserPosition userPosition = new UserPosition(id);
        return userPositionRepository.save(userPosition);
    }
    public UserPosition createUserPosition(UserPosition userPosition){
        Long id = userPosition.getId();
        List<String> positionName = userPosition.getPositionName();
        Long balance = userPosition.getBalance();
        String organization = userPosition.getOrganization();
        UserPosition newUserPosition = new UserPosition(id,positionName, balance,organization);
        organizationTableService.insertIntoOrganization(organization,"Default",id);
        return userPositionRepository.save(newUserPosition);
    }

    public UserPosition updateUserPosition(UserPosition userPosition){

        return userPositionRepository.save(userPosition);

    }


    public Optional<UserPosition> getUserPositionById(Long id){
        return userPositionRepository.findById(id);
    }
    public List<UserPosition> getAll(){
        return userPositionRepository.findAll();
    }
    public boolean deleteUserPosition(Long id){
        try{
            userPositionRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
