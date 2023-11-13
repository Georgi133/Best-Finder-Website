package softuni.WebFinderserver.services.businessServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.BlackList;
import softuni.WebFinderserver.repositories.BlackListRepository;
import softuni.WebFinderserver.services.businessServicesInt.BlackListService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;
    private Logger LOGGER = LoggerFactory.getLogger(BlackListServiceImpl.class);

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
            blackListRepository.save(new BlackList(ipAddress, LocalDateTime.now()));
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

    @Scheduled(cron = "0 00 * * * *")
    public void IfBanIsExpiredRemove () {
        List<BlackList> all = blackListRepository.getAllThatHasExpired();
        if(!all.isEmpty()){
            LOGGER.info("Successfully unbanned users with IP addresses: "
            + all.stream().map(BlackList::getBlockedIpAddress).collect(Collectors.joining(",")));
            all.forEach(e -> this.removeIpAddressFromBlackList(e.getBlockedIpAddress()));
            return;
        }
        LOGGER.info("For the moment there weren't found expired ban restrictions");
    }

}
