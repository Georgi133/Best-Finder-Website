package softuni.WebFinderserver.services.businessServices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import softuni.WebFinderserver.model.entities.BlackList;
import softuni.WebFinderserver.repositories.BlackListRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class BlackServiceImplTest {

    private final String IP_ADDRESS = "87.11";
    private final Long ID = 1L;
    @InjectMocks
    private BlackListServiceImpl toTest;

    @Mock
    private BlackListRepository blackListRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void isBlackListedShouldReturnTrue() {
        BlackList blackList = getBList();
        Mockito.when(blackListRepository.findFirstByBlockedIpAddress(IP_ADDRESS))
                .thenReturn(Optional.of(blackList));
        Assertions.assertTrue(toTest.isBlackListed(IP_ADDRESS));
    }

    @Test
    public void isBlackListedFalseWhenNoBannedUsers() {
        Mockito.when(blackListRepository.findFirstByBlockedIpAddress(IP_ADDRESS))
                .thenReturn(Optional.empty());
        Assertions.assertFalse(toTest.isBlackListed(IP_ADDRESS));
    }


    @Test
    public void addToBlackList() {
        Mockito.when(blackListRepository.findFirstByBlockedIpAddress(IP_ADDRESS))
                .thenReturn(Optional.empty());
        Assertions.assertTrue(toTest.addToBlackList(IP_ADDRESS));
    }

    @Test
    public void addToBlackListShouldBeFalseIfIpAlreadyBanned() {
        BlackList bList = getBList();
        Mockito.when(blackListRepository.findFirstByBlockedIpAddress(IP_ADDRESS))
                .thenReturn(Optional.of(bList));
        Assertions.assertFalse(toTest.addToBlackList(IP_ADDRESS));
    }

    @Test
    public void removeIpAddressFromBList() {
        BlackList bList = getBList();
        Mockito.when(blackListRepository.findFirstByBlockedIpAddress(IP_ADDRESS))
                .thenReturn(Optional.of(bList));
        Mockito.doAnswer(invocation -> {
            return null;
        }).when(blackListRepository).deleteById(ID);

        Assertions.assertTrue(toTest.removeIpAddressFromBlackList(IP_ADDRESS));
    }

    @Test
    public void removeIpAddressFromBListShouldBeFalseIfIpNotPresent() {
        Mockito.when(blackListRepository.findFirstByBlockedIpAddress(IP_ADDRESS))
                .thenReturn(Optional.empty());
        Assertions.assertFalse(toTest.removeIpAddressFromBlackList(IP_ADDRESS));
    }


    public BlackList getBList () {
        BlackList blackList = new BlackList();
        blackList.setId(ID);
        blackList.setTimeOfBan(LocalDateTime.now());
        blackList.setBlockedIpAddress(IP_ADDRESS);

        return blackList;
    }


}
