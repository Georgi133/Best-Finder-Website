package softuni.WebFinderserver.services.businessServicesInt;

public interface BlackListService {

    boolean isBlackListed(String ipAddress);
    boolean addToBlackList(String ipAddress);
    boolean removeIpAddressFromBlackList(String ipAddress);


}
