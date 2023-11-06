package softuni.WebFinderserver.services.businessServices;

import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.categories.BlackList;
import softuni.WebFinderserver.repositories.BlackListRepository;
import softuni.WebFinderserver.services.businessServicesInt.BlackListService;

@Service
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;

    public BlackListServiceImpl(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    @Override
    public boolean isBlackListed(String ipAddress) {
        return blackListRepository.findFirstByBlockedIpAddress(ipAddress).isPresent();
    }

    @Override
    public boolean addToBlackList(String ipAddress) {
        if(blackListRepository.findFirstByBlockedIpAddress(ipAddress).isEmpty()) {
            blackListRepository.save(new BlackList(ipAddress));
            return true;
        }
        return false;
    }

    @Override
    public boolean removeIpAddressFromBlackList(String ipAddress) {
         if(blackListRepository.findFirstByBlockedIpAddress(ipAddress).isPresent()) {
             blackListRepository.deleteById(blackListRepository.findFirstByBlockedIpAddress(ipAddress).get().getId());
             return true;
         }
         return false;
    }


}
